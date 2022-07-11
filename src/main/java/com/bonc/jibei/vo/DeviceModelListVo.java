package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceModelListVo {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("id数组")
    private DeviceModelVo[] idsList;
}
