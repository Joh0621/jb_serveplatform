package com.bonc.jibei.vo;

import lombok.Data;

@Data
public class NumericalStatisticsVo {
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
    private Double maxTemp;

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
    private Double minWindSpeed;

    /**
     * 地区名
     */
    private String aName;
}
