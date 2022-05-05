package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 20:06
 * @Description: 报告模板表
 */
@Data
@TableName("jb_report_model")
public class ReportModel {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("模板类型")
    private Integer modelType;

    @ApiModelProperty("模板编码")
    private String  modelCode;

    @ApiModelProperty("模板名称")
    private String  modelName;

    @ApiModelProperty("模板描述")
    private String  modelDesc;

    @ApiModelProperty("模板版本号")
    private String  modelVersion;

    @ApiModelProperty("报告名称")
    private String  reportName;

    @ApiModelProperty("模板文件名称")
    private String  modelFileName;

    @ApiModelProperty("模板文件存放地址")
    private String  modelFileUrl;

    @ApiModelProperty("模板状态;是否启用;2=已停用，1= 启用.0=待启用")
    private Integer status;

    @ApiModelProperty("起|停用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime operStatusDate;
}
