package com.bonc.jibei.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("gb_code_type")
public class CodeType {

    /**
     * 编码id
     * (主键ID)
     * (无默认值)
     */
    private Integer codeId;

    /**
     * 编码
     * (可选项)
     * (无默认值)
     */
    private String code;

    /**
     * 编码名称
     * (无默认值)
     */
    private String codeName;

    /**
     * 数值单位
     * (无默认值)
     */
    private String valueUnit;

    /**
     * 排序用编号
     * (无默认值)
     */
    private Integer sortNo;

    /**
     * 是否必填 1：必填  0：非必填
     * (无默认值)
     */
    private Integer required;


    /**
     * 示例
     * (无默认值)
     */
    private String example;
}
