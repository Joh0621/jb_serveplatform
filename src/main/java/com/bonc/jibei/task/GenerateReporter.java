package com.bonc.jibei.task;

import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.vo.ReportModelInter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
//    @Scheduled(cron = "0 0/1 * * * ?")
    //每天0点执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    public Object createReport() throws IOException {
        //先取场站模板
        int result = 0;
        List<ReportModelInter> stationModellist = reportModelInterMapper.selectReportModel(DateUtil.lastQrtYear(), DateUtil.lastQrt());
        //场站模板接口，生成报告
        if (stationModellist != null && stationModellist.size() > 0) {
            for (ReportModelInter obj : stationModellist) {
                if (obj == null) {
                    continue;
                }
                //obj.setId(0);
                try {
                    result = reportMngService.insertReport(obj);
                }finally {

                }


            }
        }
            return result;
    }
}
