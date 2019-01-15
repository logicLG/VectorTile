package edu.zju.gis.vectortile.service.impl;

import edu.zju.gis.vectortile.dao.PbfXyzMapper;
import edu.zju.gis.vectortile.dao.TrafficMapper;
import edu.zju.gis.vectortile.entity.PbfXyz;
import edu.zju.gis.vectortile.service.VectorTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VectorTileServiceImpl implements VectorTileService {
    @Autowired
    TrafficMapper trafficMapper;
    @Autowired
    PbfXyzMapper pbfXyzMapper;

    @Override
    public Map<String, Integer> getLOSByTile(String tileId) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> features = new ArrayList<>();
        List<PbfXyz> xyzList = pbfXyzMapper.selectByPrimaryKey(tileId);
        xyzList.forEach(xyz -> Collections.addAll(features, xyz.getContent().split(",")));

//        List<String> features = Arrays.asList(pbfXyzMapper.selectByPrimaryKey(pbfID).getContent().split(","));
        List<Integer> featureIds = features.stream().map(Integer::valueOf).collect(Collectors.toList());
        int batchSize = 1000;
        int offset = 0;
        while (offset < featureIds.size()) {
            int end = offset + batchSize;
            if (end > featureIds.size())
                end = featureIds.size();
            trafficMapper.selectByKeys(featureIds.subList(offset, end))
                    .forEach(feat -> resultMap.put(feat.getObjectid().toString(), feat.getLos()));
            offset = end;
        }
        return resultMap;
    }

    @Override
    public Map<String, Integer> getLOSByTile(String tileId, long timestamp) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> roads = new ArrayList<>();
        List<PbfXyz> xyzList = pbfXyzMapper.selectByPrimaryKey(tileId);
        xyzList.forEach(xyz -> Collections.addAll(roads, xyz.getContent().split(",")));
        //todo 读取历史数据 & 预测路况
        return resultMap;
    }

}
