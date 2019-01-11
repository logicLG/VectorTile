package edu.zju.gis.vectorTile.dao;

import edu.zju.gis.vectorTile.entity.PbfXyz;

import java.util.List;

public interface PbfXyzMapper {
    int deleteByPrimaryKey(String gridid);

    int insert(PbfXyz record);

    int insertSelective(PbfXyz record);

    List<PbfXyz> selectByPrimaryKey(String gridid);

    int updateByPrimaryKeySelective(PbfXyz record);

    int updateByPrimaryKeyWithBLOBs(PbfXyz record);
}