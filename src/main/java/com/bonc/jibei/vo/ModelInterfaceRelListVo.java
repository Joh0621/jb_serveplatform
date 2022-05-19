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

    @ApiModelProperty("接口编码")
    private String  InterCode;

    @ApiModelProperty("参数编码")
    private String  code;

    @ApiModelProperty("接口类型,1:数据，占位；2：列表；3：柱状图，4：折线图，5：雷达图")
    private String interType;

    @ApiModelProperty("接口名称")
    private String interName;

    @ApiModelProperty("接口描述")
    private String interDesc;
}
