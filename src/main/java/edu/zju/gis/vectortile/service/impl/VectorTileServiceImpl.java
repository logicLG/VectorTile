package edu.zju.gis.vectortile.service.impl;

import edu.zju.gis.vectortile.dao.PbfXyzMapper;
import edu.zju.gis.vectortile.dao.TrafficHistoryMapper;
import edu.zju.gis.vectortile.dao.TrafficMapper;
import edu.zju.gis.vectortile.entity.PbfXyz;
import edu.zju.gis.vectortile.entity.TrafficHistory;
import edu.zju.gis.vectortile.entity.TrafficTime;
import edu.zju.gis.vectortile.service.VectorTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

@Service
public class VectorTileServiceImpl implements VectorTileService {
    @Autowired
    TrafficMapper trafficMapper;
    @Autowired
    PbfXyzMapper pbfXyzMapper;
    @Autowired
    TrafficHistoryMapper trafficHistoryMapper;

    @Override
    @Cacheable(value = "getLOSByTile")
    public Map<String, Integer> getLOSByTile(String tileId) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> features = new ArrayList<>();
        List<PbfXyz> xyzList = pbfXyzMapper.selectByPrimaryKey(tileId);
        xyzList.forEach(xyz -> Collections.addAll(features, xyz.getContent().split(",")));

//        List<String> features = Arrays.asList(pbfXyzMapper.selectByPrimaryKey(pbfID).getContent().split(","));
        int batchSize = 1000;
        int offset = 0;
        while (offset < features.size()) {
            int end = offset + batchSize;
            if (end > features.size())
                end = features.size();
            trafficMapper.selectByRticidList(features.subList(offset, end))
                    .forEach(feat -> {
                        if(!resultMap.containsKey(feat.getRticid()))
                            resultMap.put(feat.getRticid(), feat.getLos());
                    });
            offset = end;
        }
        return resultMap;
    }

    @Override
    public Map<String, Integer> getLOSByTile(String tileId,long time) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> roads = new ArrayList<>();
        List<PbfXyz> xyzList = pbfXyzMapper.selectByPrimaryKey(tileId);
        boolean predict=false;
        if(time>System.currentTimeMillis()){
            predict=true;
        }
        xyzList.forEach(xyz -> Collections.addAll(roads, xyz.getContent().split(",")));
        if(predict==true) {
            Map<String, List<TrafficTime>> map = getDateMapFromLastMonth(time);
            Map<String, List<Integer>> roadLosList = new HashMap<>();
            map.forEach((key, value) -> {
                for (TrafficTime trafficTime : value) {
                    List<TrafficHistory> trafficHistories = trafficHistoryMapper.selectByRticidList(key, roads, trafficTime.getUpdateMaxTime(), trafficTime.getUpdateMinTime());
                    trafficHistories.stream()
                            .collect(groupingBy(TrafficHistory::getRticid, maxBy(Comparator.comparing(TrafficHistory::getUpdatetime))))
                            .forEach((k, v) -> {
                                if (roadLosList.containsKey(k)) {
                                    roadLosList.get(k).add(v.get().getLos());
                                } else {
                                    List<Integer> list = new ArrayList<>();
                                    roadLosList.put(k, list);
                                }
                            });
                }
            });
            roadLosList.forEach((key, value) -> {
                int sum = 0;
                for (Integer i : value) {
                    sum = sum + i;
                }
                int mean = (int) Math.round(sum * 1.0 / value.size());
                resultMap.put(key, mean);
            });
        }else {
            Map<String, TrafficTime> map = getDateFromLastMonth(time);
            String tableName=map.keySet().iterator().next();
            String updateMaxTime = map.get(tableName).getUpdateMaxTime();
            String updateMinTime = map.get(tableName).getUpdateMinTime();
            List<TrafficHistory> trafficHistories = trafficHistoryMapper.selectByRticidList(tableName, roads, updateMaxTime, updateMinTime);
            trafficHistories.stream()
                    .collect(groupingBy(TrafficHistory::getRticid, maxBy(Comparator.comparing(TrafficHistory::getUpdatetime))))
                    .forEach((key, value) -> resultMap.put(key, value.get().getLos()));
        }
        roads.forEach((rticid) -> {
            if (!resultMap.containsKey(rticid)) {
                resultMap.put(rticid, 1);
            }
        });
        return resultMap;
    }
    //获取该时间前四周相同时刻并转换成对应数据库中的时间格式
    public static Map<String, List<TrafficTime>> getDateMapFromLastMonth(long time) {
        Map<String, List<TrafficTime>> resultMap = new HashMap();
        List<String> result=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        for(int i=0;i<4;i++) {
            calendar.add(calendar.DATE ,-7);
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH)+1;
            String formatMonth= String.valueOf(month);
            String formatDate=String.valueOf(calendar.get(Calendar.DATE));
            String formatHour=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
            String formatMin=String.valueOf(calendar.get(Calendar.MINUTE));
            if(month<10){
                formatMonth="0"+month;
            }
            if(calendar.get(Calendar.DATE)<10){
                formatDate="0"+formatDate;
            }
            if(calendar.get(Calendar.HOUR_OF_DAY)<10){
                formatHour="0"+formatHour;
            }
            if(calendar.get(Calendar.MINUTE)<10){
                formatMin="0"+formatMin;
            }
            String tableName="RTICCHANGED_"+year+month;
            String updateMaxDate = year + formatMonth + formatDate + formatHour + formatMin;
            int minDate = calendar.get(Calendar.DATE) - 1;
            String updateMinDate;
            if (minDate == 0) {
                minDate = 1;
                updateMinDate = year + formatMonth + "0" + minDate + "00" + "00";
            } else if (minDate < 10)
                updateMinDate = year + formatMonth + "0" + minDate + formatHour + formatMin;
            else updateMinDate = year + formatMonth + minDate + formatHour + formatMin;
            TrafficTime trafficTime = new TrafficTime(updateMaxDate, updateMinDate);
            if(resultMap.containsKey(tableName)){
                resultMap.get(tableName).add(trafficTime);
            }else {
                List<TrafficTime> list = new ArrayList<>();
                list.add(trafficTime);
                resultMap.put(tableName,list);
            }
        }
        return resultMap;
    }

    public static Map<String, TrafficTime> getDateFromLastMonth(long time) {
        Map<String, TrafficTime> resultMap = new HashMap<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        String formatMonth= String.valueOf(month);
        String formatDate=String.valueOf(calendar.get(Calendar.DATE));
        String formatHour=String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String formatMin=String.valueOf(calendar.get(Calendar.MINUTE));
        if(month<10){
            formatMonth="0"+month;
        }
        if(calendar.get(Calendar.DATE)<10){
            formatDate="0"+formatDate;
        }
        if(calendar.get(Calendar.HOUR_OF_DAY)<10){
            formatHour="0"+formatHour;
        }
        if(calendar.get(Calendar.MINUTE)<10){
            formatMin="0"+formatMin;
        }
        String tableName="RTICCHANGED_"+year+month;
        int minDate = calendar.get(Calendar.DATE) - 1;
        String updateMaxDate = year + formatMonth + formatDate + formatHour + formatMin;
        String updateMinDate;
        if (minDate == 0) {
            minDate = 1;
            updateMinDate = year + formatMonth + "0" + minDate + "00" + "00";
        } else if (minDate < 10)
            updateMinDate = year + formatMonth + "0" + minDate + formatHour + formatMin;
        else updateMinDate = year + formatMonth + minDate + formatHour + formatMin;

        resultMap.put(tableName, new TrafficTime(updateMaxDate, updateMinDate));
        return resultMap;
    }
}
