package com.bonc.jibei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bonc.jibei.entity.Report;
import com.bonc.jibei.vo.ReportModelInter;


/**
 * @Author: dupengling
 * @DateTime: 2022/4/25 14:09
 * @Description: TODO
 */
public interface ReportService  extends IService<Report> {
    int insertReport(ReportModelInter reportlist);//新生成报告
    int updateReport(ReportModelInter reportlist);//重新生成报告
}
