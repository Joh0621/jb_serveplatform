package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/20 0:27
 * @Description: 模板+场站
 */
@Data
public class ModelStationIdsVo {
    @ApiModelProperty("报告id,编辑用")
    Integer reportId;

    @ApiModelProperty("报告名称")
    String reportName;

    @ApiModelProperty("模板版本号")
    String modelVersion;

    @ApiModelProperty("报告类型")
    String reportType;

    @ApiModelProperty("模板名称")
    String modelName;

    @ApiModelProperty("场站list")
    Integer[] idList;
}
