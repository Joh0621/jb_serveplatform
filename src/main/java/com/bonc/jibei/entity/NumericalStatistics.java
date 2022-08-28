package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 数值统计数据表
 * @author zhengkai.blog.csdn.net
 * @date 2022-08-25
 */
@Data
public class NumericalStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Integer id;

    /**
     * 场站id
     */
    private String stationId;

    /**
     * 年月
     */
    private String yearMonthDate;

    /**
     * 辐射量
     */
    private Double radiationDose;

    /**
     * 峰值辐射度
     */
    private Double peakIrradiance;

    /**
     * 平均温度
     */
    private Double avgTemp;

    /**
     * 最高温度
     */
    private Double maxTrmp;

    /**
     * 最低温度
     */
    private Double minTemp;

    /**
     * 平均风速
     */
    private Double avgWindSpeed;

    /**
     * 最高风速
     */
    private Double maxWindSpeed;

    /**
     * 最低风速
     */
    private Double minAvgWindSpeed;

    /**
     * 数量
     */
    private Integer cnt;

    /**
     * 更新时间
     */
    private Date updateTime;

    public NumericalStatistics() {}
}
