package com.bonc.jibei.task;

import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.mapper.ReportMngMapper;
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
 * @DateTime: 2022/4/26 16:02
 * @Description: 重新生成报告
 */
@Component
@EnableScheduling
public class ReGenerateReporter {
    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    @Resource
    private ReportMngMapper reportMngMapper;

    @Resource
    private ReportService reportService;
    //10分钟执行一次
    @Scheduled(cron = "0 0/10 * * * ?")
    public void createReport() {
        List<ReportModelInter> reportlist=reportModelInterMapper.selectReReportModelInter();
        for (ReportModelInter obj:reportlist){
            //更新报告管理的状态为 在处理
            ReportMng mng=new ReportMng();
            mng.setId(obj.getId());
            mng.setReportStatus(3);
            reportMngMapper.updateById(mng);
            reportService.updateReport(obj);
        }
    }
}
