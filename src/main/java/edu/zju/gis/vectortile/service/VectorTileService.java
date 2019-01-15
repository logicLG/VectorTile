package edu.zju.gis.vectortile.service;

import java.util.Map;

public interface VectorTileService {

    Map<String, Integer> getLOSByTile(String tileId);

    Map<String, Integer> getLOSByTile(String tileId, long timestamp);
}
