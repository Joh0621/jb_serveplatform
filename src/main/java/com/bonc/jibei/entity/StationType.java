package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 16:04
 * @Description: TODO
 */
@Data
@TableName("jb_station_type")
public class StationType {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("组织ID")
    private Integer orgId;
    @ApiModelProperty("类型编码")
    private String typeCode;
    @ApiModelProperty("类型名称")
    private String typeName;
    @ApiModelProperty("是否显示;1=显示")
    private Integer isShow;
}
