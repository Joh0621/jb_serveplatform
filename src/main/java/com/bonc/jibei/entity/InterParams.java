package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/5 16:31
 * @Description: 模板参数表
 */
@Data
@TableName("jb_inter_params")
public class InterParams {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("接口ID")
    private Integer interId;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("参数属性")
    private String paramType;

    @ApiModelProperty("参数描述")
    private String paramDesc;
}
