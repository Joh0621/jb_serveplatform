package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/19 10:03
 * @Description: 模板参数映射列表
 */
@Data
public class ModelInterfaceRelListVo {
    @ApiModelProperty("映射id")
    private Integer id;

    @ApiModelProperty("模板名称")
    private String  modelName;

    @ApiModelProperty("模板版本号")
    private String  modelVersion;

    @ApiModelProperty("模板类型")
    private Integer modelType;

    @ApiModelProperty("模板描述|说明")
    private String  modelDesc;

    @ApiModelProperty("模板文件名称")
    private String  modelFileName;

    @ApiModelProperty("模板文件存放地址")
    private String  modelFileUrl;

    @ApiModelProperty("接口编码")
    private String  InterCode;

    @ApiModelProperty("接口编码")
    private String  InterId;

    @ApiModelProperty("参数编码")
    private String  paramId;

    @ApiModelProperty("接口类型,1:数据，占位；2：列表；3：柱状图，4：折线图，5：雷达图")
    private String interType;

    @ApiModelProperty("接口名称")
    private String interName;

    @ApiModelProperty("接口描述")
    private String interDesc;

    @ApiModelProperty("参数类型")
    private String paramType;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("参数描述")
    private String paramDesc;

    @ApiModelProperty("映射参数")
    private String mapParam;
}
