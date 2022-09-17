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
    private String dataTime;

    /**
     * 辐射量
     */
    private Double radiationDose;


    private Double radiationDoseTq;
    /**
     * 峰值辐射度
     */
    private Double peakIrradiance;
    private Double peakIrradianceTq;
    /**
     * 平均温度
     */
    private Double avgTemp;
    private Double avgTempTq;

    /**
     * 最高温度
     */
    private Double maxTemp;
    private Double maxTempTq;

    /**
     * 最低温度
     */
    private Double minTemp;
    private Double minTempTq;

    /**
     * 平均风速
     */
    private Double avgWindSpeed;
    private Double avgWindSpeedTq;

    /**
     * 最高风速
     */
    private Double maxWindSpeed;
    private Double maxWindSpeedTq;

    /**
     * 最低风速
     */
    private Double minWindSpeed;
    private Double minWindSpeedTq;

    /**
     * 地区名
     */
    private String aName;
}
