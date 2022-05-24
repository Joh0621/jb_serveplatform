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
 * @DateTime: 2022/5/24 9:33
 * @Description: 报告
 */
@Data
@TableName("jb_report")
public class Report {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织")
    private Integer orgId;

    @ApiModelProperty("报告编码")
    private String  reportCode;

    @ApiModelProperty("报告类型，与场站类型同步")
    private Integer  reportType;

    @ApiModelProperty("报告名称")
    private String  reportName;

    @ApiModelProperty("报告启动状态;0=待启动;1=已启动;2=已停用")
    private Integer  status;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Integer createUserId;

    @ApiModelProperty("启用时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime  startTime;

    @ApiModelProperty("启用人")
    private Integer startUserId;
}
