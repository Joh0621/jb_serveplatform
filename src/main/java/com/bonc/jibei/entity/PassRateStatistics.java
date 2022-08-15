package com.bonc.jibei.entity;

import lombok.Data;

@Data
public class PassRateStatistics {
    /**
     * 接入数量
     */
    private Integer totalNum;

    /**
     * 合格数
     */
    private Integer qualifiedNum;

    /**
     * 总合格率
     */
    private Double qualifiedRate;

    /**
     * 风电合格率
     */
    private Double windQualifiedRate;

    /**
     * 光伏合格率
     */
    private Double pVQualifiedRate;
    /**
     * 光伏/光电类型
     */
    private  Integer typeId;
}
