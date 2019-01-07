package edu.zju.gis.vectorTile.service;

import java.util.Map;

public interface VectorTileService {

    Map<String,Integer> getFeatureLOSBypbfId(String pbfID);
}
