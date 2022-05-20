package com.bonc.jibei.vo;

import com.bonc.jibei.entity.StationModelRel;
import lombok.Data;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/20 11:26
 * @Description: 模板报告详情
 */
@Data
public class ModelReportViewVo {
     private String reportName;
     private String modelv;
     private String  reportType;
     private String modelName;
     List<StationModelRel> rel;
}
