package com.bonc.jibei.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 13:01
 * @Description: TODO
 */
@Data
@Component
@ConfigurationProperties(prefix="spring.cfg")
public class WordCfgProperties {
    //环境标识：windows or linux
    private String OSType;
    //生成的图片路径
    private String pngPath;
    //生成的word存放路径
    private String wordPath;
}
