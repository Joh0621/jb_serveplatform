package com.bonc.jibei.vo;

import com.bonc.jibei.entity.DeviceModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class DeviceModelVo {

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("设备型号")
    private String   deviceModel;

    @ApiModelProperty("设备制造商")
    private String deviceCompany;



    @ApiModelProperty("设备参数")
    private   HashMap<String, Object> data;





}
