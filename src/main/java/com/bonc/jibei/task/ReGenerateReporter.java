package com.bonc.jibei.task;

import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.service.ReportMngService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/26 16:02
 * @Description: 重新生成报告
 */
@Component
public class ReGenerateReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReGenerateReporter.class);

    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    @Resource
    private ReportMngMapper reportMngMapper;

    @Resource
    private ReportMngService reportMngService;

    //5分钟执行一次
//    @Scheduled(cron = "0 0/5 * * * ?")
    // @Async("threadPoolTaskExecutor")
    public void reCreateReport() throws IOException {
        //先取 场站模板
        Integer reportStatus = 2;
        List<ReportMng> stationModellist = reportModelInterMapper.selectReReportModel(reportStatus);
//        LOGGER.info(stationModellist.toString());
        //处理场站模板 接口 生成报告
        if (stationModellist.size() > 0) {
            for (ReportMng obj : stationModellist) {
                if (obj == null) {
                    continue;
                }
                //更新报告管理的状态为 在处理
                obj.setReportStatus(3);
                reportMngMapper.updateById(obj);
                reportMngService.updateReport(obj);
            }
        }

    }
}
