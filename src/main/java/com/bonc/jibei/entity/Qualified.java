package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @description 数据质量条数表
 * @author wangtao
 * @date 2022-08-08
 */
@Data
public class Qualified implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Integer id;

    /**
     * 总条数
     */
    private Integer totalNum;

    /**
     * 合格条数
     */
    private Integer qualifiedNum;

    /**
     * 不合格数
     */
    private Integer unQualifiedNum;

    /**
     * 日期
     */
    private Date dateTime;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 场站id
     */
    private Integer stationId;

    private Integer hlpt;
    private Integer djxt;
    private Integer czsssj;
    private Integer glycsj;
    private Double passRate;


}