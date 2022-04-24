package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 14:06
 * @Description: TODO
 */
@Data
public class ReportMngPatchPub {

//    @ApiModelProperty("报告id")
//    private Integer id;

    @ApiModelProperty("报告状态,1=发布.0=没发布")
    private Integer reportStatus;

    @ApiModelProperty("需修改报告id")
    private Integer[] idsList;

}
