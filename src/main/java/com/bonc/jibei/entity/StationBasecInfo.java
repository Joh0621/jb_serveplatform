package com.bonc.jibei.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class StationBasecInfo {
    private Integer id;
    private Integer pid;

    @ApiModelProperty("场站类型")
    private String czbasicLX;

    @ApiModelProperty("场站名")
    private String czbasicName;

    @ApiModelProperty("所属区域")
    private String czbasicDQ;

    @ApiModelProperty("所属集团")
    private String czbasicSSJT;

    @ApiModelProperty("装机容量(MW)")
    private String czbasicZJRL;

    @ApiModelProperty("风机/逆变器数量(台")
    private String czbasicJZSL;

    @ApiModelProperty("电压等级")
    private String czbasicDYDJ;


    @ApiModelProperty("所属调度机构")
    private String czbasicDDJG;

    @ApiModelProperty("接入电网")
    private String czbasicJJDW;

    @ApiModelProperty("投运状态")
    private String czbasicTYZT;

    @ApiModelProperty("设计等效小时数")
    private String czbasicDDXS;

    @ApiModelProperty("投运日期")
    private String czbasicTYRQ;

    @ApiModelProperty("详细地址")
    private String czbasicADD;

    @ApiModelProperty("联系电话")
    private String czbasicZTELL;


    @ApiModelProperty("地理位置")
    private String czbasicDLWZ;
    @ApiModelProperty("海拔高度")

    private String czbasicHBGD;


    @ApiModelProperty("开关布置方式")
    private String czbasicBZFS;

    @ApiModelProperty("母线接线方式")
    private String czbasicJXFS;

    @ApiModelProperty("汇集系统中性点接地方式")
    private String czbasicJDFS;

    @ApiModelProperty("滤波器数量")
    private String czbasicLBQSL;

    @ApiModelProperty("无功补偿器容量")
    private String czbasicBCQSL;

    @ApiModelProperty("测风塔数量")
    private String czbasicCFTSL;

    @ApiModelProperty("并网变电站")
    private String czbasicBWBDZ;


    @ApiModelProperty("是否显示1=显示")
    private Integer isShow;
}
