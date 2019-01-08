package edu.zju.gis.vectorTile.service.impl;

import edu.zju.gis.vectorTile.dao.FeaturesMapper;
import edu.zju.gis.vectorTile.dao.PbfXyzMapper;
import edu.zju.gis.vectorTile.entity.PbfXyz;
import edu.zju.gis.vectorTile.service.VectorTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VectorTileServiceImpl implements VectorTileService {
    @Autowired
    FeaturesMapper featuresMapper;
    @Autowired
    PbfXyzMapper pbfXyzMapper;

    @Override
    public Map<Integer, Integer> getFeatureLOSBypbfId(String pbfID) {
        Map<Integer, Integer> resultMap = new HashMap<>();
        PbfXyz pbfXyz = null;
        String content = null;
        try {
            pbfXyz = pbfXyzMapper.selectByPrimaryKey(pbfID);
            content = pbfXyz.getContent();
        } catch (Exception ex) {
            System.out.println(pbfID + "为空");
            return resultMap;
        }

        List<String> features = Arrays.asList(content.split(","));
        List<Integer> featureIds = features.stream().map(Integer::valueOf).collect(Collectors.toList());
        int batchSize = 1000;
        int offset = 0;
        while (offset < featureIds.size()) {
            int end = offset + batchSize;
            if (end > featureIds.size())
                end = featureIds.size();
            featuresMapper.selectByKeys(featureIds.subList(offset, end))
                    .forEach(feat -> resultMap.put(feat.getUuid(), feat.getLos()));
            offset = end;
        }
//        features.forEach(x -> {
//            Features features = featuresMapper.selectByPrimaryKey(Integer.valueOf(x));
//            resultMap.put(features.getUuid().toString(), features.getLos());
//        });
        return resultMap;
    }
}
