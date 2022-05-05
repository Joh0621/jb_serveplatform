package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName("jb_model_params_map")
public class ReportParamsMap {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("模板ID")
    private Integer modelId;

    @ApiModelProperty("接口ID")
    private Integer interId;

    @ApiModelProperty("参数")
    private String param;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("映射参数")
    private String mapParam;
}
