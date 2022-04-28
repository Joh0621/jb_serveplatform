package com.bonc.jibei.task;

import com.bonc.jibei.mapper.ReportModelInterMapper;

import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.vo.ReportModelInter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 22:17
 * @Description: 自动生成报告
 */
@Component
@EnableScheduling
public class GenerateReporter {
    @Resource
    private ReportModelInterMapper reportModelInterMapper;
    @Resource
    private ReportMngService reportService;
    //10分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void createReport() {
        List<ReportModelInter> reportlist=reportModelInterMapper.selectReportModelInter(DateUtil.lastQrtYear(),DateUtil.lastQrt());
        for (ReportModelInter obj:reportlist){
            reportService.insertReport(obj);
        }
    }
}
