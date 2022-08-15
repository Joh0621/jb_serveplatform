package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/19 15:55
 * @Description: TODO
 */
@Data
public class ModelInterParamMapVo {
    @ApiModelProperty("映射id")
    private Integer id;

    @ApiModelProperty("模板名称")
    private String  modelName;

    @ApiModelProperty("接口编码")
    private String  interId;

    @ApiModelProperty("参数编码")
    private String  code;

    @ApiModelProperty("接口类型,1:数据，占位；2：列表；3：柱状图，4：折线图，5：雷达图")
    private String interType;

    @ApiModelProperty("接口名称")
    private String interName;

    @ApiModelProperty("接口描述")
    private String interDesc;

    @ApiModelProperty("参数类型")
    private Integer paramType;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("混合参数名")
    private String mixName;

    @ApiModelProperty("参数描述")
    private String paramDesc;

    @ApiModelProperty("参数类型名")
    private String paramTypeName;

    @ApiModelProperty("映射参数")
    private String mapParam;
}
