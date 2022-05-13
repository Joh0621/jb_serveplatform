package com.bonc.jibei.api;

/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/4/29 14:43
 */
public interface ValueType {

    // 普通文字
    String NORMAL = "normal";
    // 表格
    String TABLE = "table";
    // 混合，文字，表格，图
    String MIX = "mix";
    String PIE = "pie";
    String BAR_GROUP = "barGroup";
    // 柱状图
    String BAR = "bar";
    String STACKED_BARE = "stackedBare";
    String LINE = "line";
    String RADAR = "radar";

}
