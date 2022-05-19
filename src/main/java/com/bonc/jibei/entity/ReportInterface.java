package com.bonc.jibei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 19:07
 * @Description: 报告接口表
 */
@Data
@TableName("jb_report_interface")
public class ReportInterface {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织ID")
    private Integer orgId;

    @ApiModelProperty("接口编码")
    private String code;

    @ApiModelProperty("接口类型,1:数据，占位；2：列表；3：柱状图，4：折线图，5：雷达图")
    private Integer interType;

    @ApiModelProperty("接口名称")
    private String interName;

    @ApiModelProperty("接口描述")
    private String interDesc;

    @ApiModelProperty("接口地址")
    private String interUrl;

    @ApiModelProperty("在模板的占位符")
    private String placeTag;
}
