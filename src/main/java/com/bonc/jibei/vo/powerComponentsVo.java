package com.bonc.jibei.vo;

import lombok.Data;

@Data
public class powerComponentsVo {


    private  Integer errorCnt;
    private  String inverter;
    private  Integer componentsErrotCnt;
    private  Double componentsErrotRate;

    private  Integer componentsStringErrotCnt;
    private  Double componentsStringErrotRate;
    private  String errorType;
}
