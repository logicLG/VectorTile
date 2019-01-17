package edu.zju.gis.vectortile.dao;

import edu.zju.gis.vectortile.entity.TrafficHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrafficHistoryMapper {
    List<TrafficHistory> selectByPrimaryKey(@Param("tableName") String tableName, @Param("value") int value);

    List<TrafficHistory> selectByKeys(@Param("tableName") String tableName, @Param("idList") List<Integer> idList);

    TrafficHistory selectByRticid(@Param("tableName") String tableName, @Param("value") String rticid, @Param("updateTime") String time);

    List<TrafficHistory> selectByRticidList(@Param("tableName") String tableName, @Param("rticidList") List<String> rticidList);
}
