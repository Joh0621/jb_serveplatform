package com.bonc.jibei.api;

public enum ErrorTypeEnum {
    MISS(1 ,"缺数"),
    DEAD(2, "死数"),
    ERROR(3, "错数"),

    ;


    private final int code;
    private final String message;
    private ErrorTypeEnum(int code, String message) {
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
