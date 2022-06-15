package com.bonc.jibei.entity;


import lombok.Data;

@Data
public class PropertyType {

    /**
     * 编码id
     * (主键ID)
     * (无默认值)
     */
    private Integer id;

    /**
     * 类型编码
     * (可选项)
     * (无默认值)
     */
    private String type_code;

    /**
     * 类型名称
     * (无默认值)
     */
    private String type_name;


}
