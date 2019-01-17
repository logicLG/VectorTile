package edu.zju.gis.vectortile.service.impl;

import edu.zju.gis.vectortile.dao.PbfXyzMapper;
import edu.zju.gis.vectortile.dao.TrafficHistoryMapper;
import edu.zju.gis.vectortile.dao.TrafficMapper;
import edu.zju.gis.vectortile.entity.PbfXyz;
import edu.zju.gis.vectortile.entity.Traffic;
import edu.zju.gis.vectortile.entity.TrafficHistory;
import edu.zju.gis.vectortile.service.VectorTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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
            Map<String,List<String>> map=getDateMapFromLastMonth(time);
            roads.forEach(rticid -> {
                int LOSCount=0;
                for(Map.Entry<String,List<String>> entry:map.entrySet()){
                    for (String updateTime : entry.getValue()) {
                        TrafficHistory traffic = trafficHistoryMapper.selectByRticid(entry.getKey(), rticid, updateTime);
                        LOSCount+=traffic.getLos();
                    }
                }
                int LOSAverage= (int) Math.round(LOSCount/4.0);
                if(LOSAverage>3||LOSAverage<1){
                    LOSAverage=1;
                }
                resultMap.put(rticid,LOSAverage);
            });
        }else {
            Map<String,String> map=getDateFromLastMonth(time);
            String tableName=map.keySet().iterator().next();
            String updateTime=map.get(tableName);
            roads.forEach(rticid -> {
                TrafficHistory traffic = trafficHistoryMapper.selectByRticid(tableName, rticid, updateTime);
                resultMap.put(rticid,traffic.getLos());
            });
        }
        return resultMap;
    }
    //获取该时间前四周相同时刻并转换成对应数据库中的时间格式
    public static Map<String,List<String>> getDateMapFromLastMonth(long time){
        Map<String,List<String>> resultMap=new HashMap();
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
            String updateDate=year+formatMonth+formatDate+formatHour+formatMin;
            if(resultMap.containsKey(tableName)){
                resultMap.get(tableName).add(updateDate);
            }else {
                List<String> list=new ArrayList<>();
                list.add(updateDate);
                resultMap.put(tableName,list);
            }
        }
        return resultMap;
    }

    public static Map<String,String> getDateFromLastMonth(long time){
        Map<String,String> resultMap=new HashMap<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
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
        resultMap.put(tableName,year+formatMonth+formatDate+formatHour+formatMin);
        return resultMap;
    }
}
