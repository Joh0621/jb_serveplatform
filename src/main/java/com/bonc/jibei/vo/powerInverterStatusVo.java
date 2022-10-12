package com.bonc.jibei.vo;

import lombok.Data;

@Data
public class powerInverterStatusVo {
    private  Integer directStatus;

    private  String conversionEfficiencyStatus;

    private  String inverterLossStatus;

    private  String threeStatus;

    private  String  tempStatus;
    private  String  sumStatus;
    private  String  overallStatus;
}
