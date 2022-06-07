package com.bonc.jibei.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class ReportModelVo {

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("模板类型")
    private Integer modelType;

    @ApiModelProperty("模板类型名称")
    private String modelTypeName;

    @ApiModelProperty("模板编码")
    private String  modelCode;

    @ApiModelProperty("模板名称")
    private String  modelName;

    @ApiModelProperty("模板描述|说明")
    private String  modelDesc;

    @ApiModelProperty("模板版本号")
    private String  modelVersion;

    @ApiModelProperty("报告编码")
    private String  reportCode;

    @ApiModelProperty("报告Id")
    private String  reportId;

    @ApiModelProperty("报告名称")
    private String  reportName;

    @ApiModelProperty("报告类型")
    private String  reportType;


    @ApiModelProperty("模板文件名称")
    private String  modelFileName;

    @ApiModelProperty("模板文件存放地址")
    private String  modelFileUrl;

    @ApiModelProperty("模板状态;是否启用;2=已停用，1= 启用.0=待启用")
    private Integer status;

    @ApiModelProperty("模板 启|停用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate operStatusDate;

    @ApiModelProperty("报告状态;是否启用;2=已停用，1= 启用.0=待启用")
    private Integer reportStatus;

    @ApiModelProperty("报告状态;是否启用;2=已停用，1= 启用.0=待启用")
    private String reportStatusName;

    @ApiModelProperty("报告 启|停用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportStatusDate;

    @ApiModelProperty("模板文件，上传文件时用，其它情况时空null")
    @TableField(exist = false)
    private MultipartFile modelFile;
}
