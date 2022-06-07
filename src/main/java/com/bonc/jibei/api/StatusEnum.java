package com.bonc.jibei.api;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/20 13:02
 * @Description: TODO
 */
public enum StatusEnum {
    INTER_TYPE_NOCHK("0", "待启动"),
    INTER_TYPE_START("1", "已启用"),
    INTER_TYPE_STOP("2", "已停用"),
    ;
    private final String  code;
    private final String name;

    private StatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
}
