package edu.zju.gis.vectorTile.service;

import java.util.Map;

public interface VectorTileService {

    Map<Integer, Integer> getFeatureLOSBypbfId(String pbfID);
}
