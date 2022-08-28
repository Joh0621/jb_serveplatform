package com.bonc.jibei.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * @description 水平辐射度概率分布表
 * @author zhengkai.blog.csdn.net
 * @date 2022-08-25
 */
public class RadiationDoseDistributed implements Serializable {

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
    private Double value;

    /**
     * 个数
     */
    private Integer cnt;

    /**
     * 场站id
     */
    private Integer stationId;


    public RadiationDoseDistributed() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

}
