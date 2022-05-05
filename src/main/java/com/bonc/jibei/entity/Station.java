package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 13:34
 * @Description: 场站表
 */
@Data
@TableName("jb_station")
public class Station {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("场站ID")
    private Integer stationId;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("场站类型")
    private Integer stationType;

    @ApiModelProperty("场站名")
    private String stationName;

    @ApiModelProperty("是否显示1=显示")
    private Integer isShow;
}
