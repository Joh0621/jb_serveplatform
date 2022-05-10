package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 12:35
 * @Description: 报告管理表
 */
@Data
@Table(name="jb_report_mng")
@TableName("jb_report_mng")
public class ReportMng {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("组织ID")
    private Integer orgId;
    @ApiModelProperty("场站ID")
    private Integer stationId;
    @ApiModelProperty("场站类型")
    private Integer stationType;

    @ApiModelProperty("报告年份")
    private Integer reportYear;

    @ApiModelProperty("报告季度")
    private Integer reportQuarter;

    @ApiModelProperty("报告名")
    private String reportName;

    @ApiModelProperty("报告文件存放位置")
    private String reportUrl;
    @ApiModelProperty("生成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    @ApiModelProperty("生成人")
    private Integer createUserId;
    @ApiModelProperty("发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime releaseTime;
    @ApiModelProperty("发布人")
    private Integer releaseUserId;
    @ApiModelProperty("模板版本号")
    private String modelVersion;
    @ApiModelProperty("报告状态;0=待复核;1=发布")
    private Integer reportStatus;

    @ApiModelProperty("状态改变时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime statusTime;
}
