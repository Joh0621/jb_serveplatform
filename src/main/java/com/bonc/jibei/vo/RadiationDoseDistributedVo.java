package com.bonc.jibei.vo;



import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @description 水平辐射度概率分布表
 * @author zhengkai.blog.csdn.net
 * @date 2022-08-25
 */
@Data
public class RadiationDoseDistributedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 年月日
     */
    private Date dataTime;

    /**
     * 水平辐照度
     */
    private String value;

    /**
     * 个数
     */
    private Integer cnt;

    /**
     * 场站id
     */
    private Integer stationId;

    /**
     * 占比
     */
    private  double rate;


    private  String aName;

}

