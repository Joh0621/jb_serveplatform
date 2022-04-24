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
 * @DateTime: 2022/4/20 16:17
 * @Description: TODO
 */
@Data
@TableName("jb_report_auth_log")
public class ReportAuthLog {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("场站ID")
    private Integer id;
    @ApiModelProperty("报告ID")
    private Integer reportId;
    @ApiModelProperty("操作")
    private String operName;
    @ApiModelProperty("操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operTime;
    @ApiModelProperty("操作人")
    private String operUser;
    @ApiModelProperty("意见")
    private String memo;
}
