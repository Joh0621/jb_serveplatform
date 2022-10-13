package com.bonc.jibei.entity;

import lombok.Data;

@Data
public class powerInverterWarning {
    private  Integer id;

    private  String stationId;
    private  String inverter;
    private  String warningTime;
    private  String warningType;
    private  String warningDesc;
    private  String parameterName;
    private  String unit;
    private  String realValue;
    private  String healthValue;
    private  String thresholdValue;
    private  String status;
}
