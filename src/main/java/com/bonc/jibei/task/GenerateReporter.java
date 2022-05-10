package com.bonc.jibei.task;

import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.vo.ReportModelInter;
import freemarker.template.TemplateException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.validation.constraints.AssertFalse;
import java.io.IOException;
import java.util.List;
/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 22:17
 * @Description: 自动生成报告，每天凌晨执行
 */
@Component
public class GenerateReporter {
    @Resource
    private ReportModelInterMapper reportModelInterMapper;
    @Resource
    private ReportMngService reportMngService;
    //1分钟执行一次
    //@Scheduled(cron = "0 0/1 * * * ?")
    //每天0点执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    public void createReport() throws TemplateException, IOException {
        //先取场站模板
        List<ReportModelInter> StationModellist=reportModelInterMapper.selectReportModel(DateUtil.lastQrtYear(),DateUtil.lastQrt());
        //场站模板接口，生成报告
        for (ReportModelInter obj:StationModellist){
            reportMngService.insertReport(obj);
        }
       /*
        for (int i=1;i<=50;i++){
            ReportModelInter obj=new ReportModelInter();
            obj.setModelId(i);
            obj.setId(i);
            reportService.insertReport(obj);
        }
        */
    }
}
