package edu.zju.gis.vectortile.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrafficTime {
    private String updateMaxTime;
    private String updateMinTime;

    public TrafficTime(String updateMaxTime, String updateMinTime) {
        this.updateMaxTime = updateMaxTime;
        this.updateMinTime = updateMinTime;
    }
}
