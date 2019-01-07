package edu.zju.gis.vectorTile.dao;

import edu.zju.gis.vectorTile.entity.PbfXyz;

public interface PbfXyzMapper {
    int deleteByPrimaryKey(String gridid);

    int insert(PbfXyz record);

    int insertSelective(PbfXyz record);

    PbfXyz selectByPrimaryKey(String gridid);

    int updateByPrimaryKeySelective(PbfXyz record);

    int updateByPrimaryKeyWithBLOBs(PbfXyz record);
}