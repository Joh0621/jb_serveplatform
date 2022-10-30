package com.bonc.jibei.entity;

import lombok.Data;

@Data
public class powerComponents {
    private  String id;
    private  String stationId;
    private  String powerUnit;
    private  String inverter;
    private  String components;
    private  String componentsLocation;
    private  String errorType;

    private  String status;
}
