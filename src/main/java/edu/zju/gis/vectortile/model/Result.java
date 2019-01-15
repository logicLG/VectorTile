package edu.zju.gis.vectortile.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result<T> {
    private String status;
    private String message;
    private T body;
}
