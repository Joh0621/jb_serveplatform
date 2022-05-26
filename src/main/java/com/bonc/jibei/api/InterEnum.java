package com.bonc.jibei.api;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 14:54
 * @Description: 接口类型枚举值
 */
public enum InterEnum {
    INTER_TYPE_NORMAL("1", "普通占位符"),
    INTER_TYPE_TABLE("2", "表格"),
    INTER_TYPE_BAR("3", "柱状图"),
    INTER_TYPE_PIE("4", "饼图"),
    INTER_TYPE_LINE("5", "线图"),
    INTER_TYPE_RADAR("6", "雷达图"),
    INTER_TYPE_BARS("7", "多组柱状图"),
    INTER_TYPE_STACKEDBARE("8", "堆叠图"),
    INTER_TYPE_MIX("11", "混合"),
    ;

    private final String code;
    private final String name;

    private InterEnum(String code, String name) {
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
