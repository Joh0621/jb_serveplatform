package com.bonc.jibei.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class DeviceModel {

    private Integer id;

    @ApiModelProperty("设备型号Id")
    private String modelCode;

    @ApiModelProperty("设备型号")
    private String   modelName;

    @ApiModelProperty("设备制造商")
    private String deviceCompany;

    @ApiModelProperty("设备分类")
    private String category;

    @ApiModelProperty("设备参数")
    private String code;

    @ApiModelProperty("设备数据")
    private String codeValue;

    @ApiModelProperty("创建人")
    private String create;

    @ApiModelProperty("启用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    private  String device_type;


}
