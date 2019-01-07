package edu.zju.gis.vectorTile.dao;

import edu.zju.gis.vectorTile.entity.Features;

import java.util.List;

public interface FeaturesMapper {
    int deleteByPrimaryKey(Integer uuid);

    int insert(Features record);

    int insertSelective(Features record);

    Features selectByPrimaryKey(Integer uuid);

    List<Features> selectByKeys(List<Integer> idList);

    int updateByPrimaryKeySelective(Features record);

    int updateByPrimaryKey(Features record);
}