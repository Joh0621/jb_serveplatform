package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 20:20
 * @Description: 场站与模板关系表
 */
@Data
@TableName("jb_station_model_rel")
public class StationModelRel {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("场站ID")
    private Integer stationId;

    @ApiModelProperty("模板ID")
    private Integer modelId;
}
