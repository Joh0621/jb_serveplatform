package com.bonc.jibei.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 23:38
 * @Description: TODO
 */
@Data
public class IdlistVo {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("id数组")
    private Integer[] idsList;
}
