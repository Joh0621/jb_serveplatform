package com.bonc.jibei.api;

import lombok.Data;


public enum DataSourceEnum {
    HLPT(1 ,"海量平台"),
    DDXT(2, "单机系统"),
    CZSSSJ(3, "场站实时数据"),
    GLYCSJ(4, "功率预测数据"),
    ;

    private final int code;
    private final String message;
    private DataSourceEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
