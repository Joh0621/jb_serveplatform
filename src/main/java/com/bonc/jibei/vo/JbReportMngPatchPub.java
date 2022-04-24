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
public class JbReportMngPatchPub {

//    @ApiModelProperty("报告id")
//    private Integer id;

    @ApiModelProperty("报告状态,1=发布.0=没发布")
    private Integer reportStatus;

    @ApiModelProperty("需修改报告id")
    private Integer[] idsList;

}
