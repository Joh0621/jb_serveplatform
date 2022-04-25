package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 20:17
 * @Description: 报告模板与接口表
 */
@Data
@TableName("jb_model_interface_rel")
public class ModelInterfaceRel {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("模板ID")
    private Integer modelId;

    @ApiModelProperty("接口iID")
    private Integer interId;
}
