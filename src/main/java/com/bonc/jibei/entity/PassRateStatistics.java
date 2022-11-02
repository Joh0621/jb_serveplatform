package com.bonc.jibei.entity;

import lombok.Data;

@Data
public class PassRateStatistics {
    /**
     * 接入数量
     */
    private Double totalNum;

    /**
     * 合格数
     */
    private Double qualifiedNum;

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
     * 风电合格数
     */
    private Double windQualifiedNum;

    /**
     * 光伏合格数
     */
    private Double pVQualifiedNum;
    /**
     * 光伏/光电类型
     */
    private  Integer typeId;

    //光伏或者风电排名
    private  String pm;
    //冀北排名
    private  String jbpm;

    private  String czpm;
}
