package com.bonc.jibei.service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bonc.jibei.entity.Report;
import com.bonc.jibei.vo.ReportModelInter;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/25 14:09
 * @Description: TODO
 */
public interface ReportService  extends IService<Report> {
    int insertReport(List<ReportModelInter> reportlist);//新生成报告
    int updateReport(JSONObject jsonObject);//重新生成报告
}
