package com.bonc.jibei.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 15:06
 * @Description: TODO
 */
@Data
@AllArgsConstructor
public class KeyValueVO {
    private String key;
    private String value;

    public KeyValueVO() {}
}
