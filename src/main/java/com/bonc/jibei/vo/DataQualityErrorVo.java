package com.bonc.jibei.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DataQualityErrorVo {
    private Integer id;

    /**
     * 日期
     */
    private Date date;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 场站id
     */
    private String stationId;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 编码
     */
    private String code;

    /**
     * 异常类型
     */
    private String errorType;

    /**
     * 条数
     */
    private Integer num;

    private String hlpt;
    private String djxt;
    private String czsssj;
    private String glycsj;
    private Double passRate;

    private String missData;
    private String deadData;
    private String errorData;

    private String stationName;
}
