package com.bonc.jibei.entity;

import lombok.Data;

import java.sql.Date;

/**
 * 逆变器异常
 */
@Data
public class powerInverter {
    private  Integer id;

    private  String stationId;

    private  String powerUnit;

    private  String inverter;

    private  Integer directStatus;

    private  String conversionEfficiency;

    private  String inverterLoss;

    private  String threeStatus;

    private  String  temp;
    private  String  overallStatus;
    private Date dataTime;
}
