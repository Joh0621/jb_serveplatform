package com.bonc.jibei.entity;


import lombok.Data;

@Data
public class Property {
    /**
     * id
     * (主键ID)
     * (无默认值)
     */
    private Integer id;
    /**
     * 父级id,场站或者设备;
     * (可选项)
     * (无默认值)
     */
    private Integer pid;
    /**
     * 属性名
     * (可选项)
     * (无默认值)
     */
    private String proName;
    /**
     * 属性编码
     * (可选项)
     * (无默认值)
     */
    private String proCode;
    /**
     * 数据来源,0代表本表，1代表从device表的台账数据
     * (可选项)
     * (无默认值)
     */
    private Integer type_code;

    /**
     * 点码
     * (可选项)
     * (无默认值)
     */
    private Integer point_code;
    /**
     * -1：不执行； 0:执行；1：上头地特殊脚本
     * (非null)
     * (默认值-1)
     */
    private Integer status;

}
