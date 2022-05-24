package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/24 9:27
 * @Description: 报告配置
 */
@Data
@TableName("jb_report_cfg")
public class ReportCfg {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("模板ID")
    private Integer modelId;

    @ApiModelProperty("报告ID")
    private Integer reportId;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Integer createUserId;

    @ApiModelProperty("状态改变时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;

    @ApiModelProperty("状态改变人")
    private Integer startUserId;
}
