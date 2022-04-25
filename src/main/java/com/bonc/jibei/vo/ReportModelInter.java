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
    @ApiModelProperty("场站ID")
    private Integer stationId;

    @ApiModelProperty("场站类型")
    private Integer stationType;

    @ApiModelProperty("模板ID")
    private Integer modelId;

    @ApiModelProperty("模板版本号")
    private Integer modelVersion;

    @ApiModelProperty("模板版文件地址")
    private String modelFileUrl;

    @ApiModelProperty("接口地址")
    private String interUrl;
}
