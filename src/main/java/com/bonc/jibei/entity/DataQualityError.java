package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description data_quality_error
 * @author wangtao
 * @date 2022-08-08
 */
@Data
public class DataQualityError implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
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
    private Integer stationId;

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

    public DataQualityError() {}
}
