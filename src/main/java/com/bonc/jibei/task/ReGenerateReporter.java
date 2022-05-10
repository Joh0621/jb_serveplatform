package com.bonc.jibei.task;

import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.vo.ReportModelInter;
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
public class ReGenerateReporter {
    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    @Resource
    private ReportMngMapper reportMngMapper;

    @Resource
    private ReportMngService reportMngService;
    //5分钟执行一次
    @Scheduled(cron = "0 0/5 * * * ?")
    public void createReport() {
        //先取 场站模板
        Integer reportStatus=2;
        List<ReportModelInter> StationModellist=reportModelInterMapper.selectReReportModel(reportStatus);

        //处理场站模板 接口 生成报告
        for (ReportModelInter obj:StationModellist){
            //更新报告管理的状态为 在处理
            ReportMng mng=new ReportMng();
            mng.setId(obj.getId());
            mng.setReportStatus(3);
            reportMngMapper.updateById(mng);
            reportMngService.updateReport(obj,mng);


        }

    }
}
