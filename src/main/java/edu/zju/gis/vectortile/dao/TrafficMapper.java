package edu.zju.gis.vectortile.dao;

import edu.zju.gis.vectortile.entity.Traffic;

import java.util.List;

public interface TrafficMapper {
    Traffic selectByPrimaryKey(Integer objectid);

    List<Traffic> selectByKeys(List<Integer> idList);

    List<Traffic> selectByRticid(String rticid);

    List<Traffic> selectByRticidList(List<String> rticidList);

}