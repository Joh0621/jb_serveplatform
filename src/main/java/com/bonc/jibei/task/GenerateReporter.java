package com.bonc.jibei.task;

import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.service.ReportService;
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
    private ReportService reportService;
    //10分钟执行一次
    @Scheduled(cron = "0 0/10 * * * ?")
    public void createReport() {
        List<ReportModelInter> reportlist=reportModelInterMapper.selectReReportModelInter(0);
        for (ReportModelInter obj:reportlist){
            reportService.insertReport(obj);
        }
    }
}
