package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 22:36
 * @Description: 参数列表
 */
@Data
public class InterParamsList {
    @ApiModelProperty("参数ID")
    private Integer id;

    @ApiModelProperty("接口ID")
    private Integer interId;

    @ApiModelProperty("接口编码")
    private String code;

    @ApiModelProperty("接口类型,1:数据；2：列表；3：柱状图，4：折线图，5：雷达图，6:堆叠图")
    private Integer interType;

    @ApiModelProperty("接口名称")
    private String interName;

    @ApiModelProperty("参数")
    private String param;

    @ApiModelProperty("参数属性")
    private String paramAttr;

    @ApiModelProperty("参数名")
    private String paraName;
}
