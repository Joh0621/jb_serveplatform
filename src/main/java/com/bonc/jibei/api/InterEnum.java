package com.bonc.jibei.api;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 14:54
 * @Description: 接口类型枚举值
 */
public enum InterEnum {
    INTER_TYPE_NORMAL("normal", "普通占位符"),
    INTER_TYPE_TABLE("table", "表格"),
    INTER_TYPE_MIX("mix", "混合"),
    INTER_TYPE_PIE("pie", "饼图"),
    INTER_TYPE_BAR("bar", "柱状图"),
    INTER_TYPE_STACKEDBARE("stackedBare", "堆叠图"),
    INTER_TYPE_LINE("line", "线图"),
    INTER_TYPE_RADAR("radar", "雷达图"),
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
