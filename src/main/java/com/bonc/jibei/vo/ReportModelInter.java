package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 22:01
 * @Description: TODO
 */
@Data
public class ReportModelInter {

    @ApiModelProperty("报告管理id")
    private Integer id;

    @ApiModelProperty("场站ID")
    private Integer stationId;

    @ApiModelProperty("报告名称")
    private String reportName;

    @ApiModelProperty("场站类型")
    private Integer stationType;

    @ApiModelProperty("模板ID")
    private Integer modelId;

    @ApiModelProperty("年份")
    private Integer reportYear;

    @ApiModelProperty("季度")
    private Integer reportQuarty;

    @ApiModelProperty("模板版本号")
    private String modelVersion;

    @ApiModelProperty("模板版文件地址")
    private String modelFileUrl;

    @ApiModelProperty("接口地址")
    private String interUrl;
    @ApiModelProperty("模板占位符,列表和图表用")
    private String placeTag	;
}
