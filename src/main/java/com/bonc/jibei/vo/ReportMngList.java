package com.bonc.jibei.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 14:06
 * @Description: TODO
 */
@Data
public class ReportMngList {
    private Integer id;
    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("场站ID")
    private Integer stationId;

    @ApiModelProperty("场站名")
    private String stationName;

    @ApiModelProperty("场站类型值")
    private String stationType;

    @ApiModelProperty("场站类型名")
    private String stationTypeName;

    @ApiModelProperty("报告年份")
    private Integer reportYear;

    @ApiModelProperty("报告季度")
    private Integer reportQuarter;

    @ApiModelProperty("报告名")
    private String reportName;

    @ApiModelProperty("报告文件存放位置")
    private String reportUrl;

    @ApiModelProperty("生成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("生成人名称")
    private Integer createUserName;

    @ApiModelProperty("发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseTime;

    @ApiModelProperty("发布人名称")
    private Integer releaseUserName;

    @ApiModelProperty("模板版本号")
    private String modelVersion;

    @ApiModelProperty("报告状态值")
    private Integer reportStatus;

    @ApiModelProperty("报告状态名")
    private Integer reportStatusName;

}
