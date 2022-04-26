package com.bonc.jibei.task;

import com.bonc.jibei.mapper.ReportModelInterMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 22:17
 * @Description: 自动生成报告
 */
@Component
public class GenerateReporter {
    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    //10分钟执行一次
    @Scheduled(cron = "0 0/10 * * * ?")
    public void createReport() {

    }
}
