package com.bonc.jibei.vo;


import com.bonc.jibei.entity.DeviceModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CodeTypeVo {

    /**
     * id
     * (无默认值)
     */
    private Integer id;


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

    /**
     * 父级id,场站或者设备;
     * (可选项)
     * (无默认值)
     */
    private Integer pid;

    /**
     * 编码信息，数值或文字;
     * (可选项)
     * (无默认值)
     */
    private String codeDetail;
    /**
     * 数据来源,0代表本表，1代表从device表的台账数据
     * (可选项)
     * (无默认值)
     */
    private Integer dataSources;


    @ApiModelProperty("设备分类信息")
    private List<DeviceModel> category;
}
