package com.bonc.jibei.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.vo.ReportModelInter;
import freemarker.template.TemplateException;
import org.springframework.scheduling.annotation.Async;
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
    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    @Resource
    private ReportMngMapper reportMngMapper;

    @Resource
    private ReportMngService reportMngService;
    //5分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
   // @Async("threadPoolTaskExecutor")
    public void createReport() throws TemplateException, IOException {
        //先取 场站模板
        Integer reportStatus=2;
        List<ReportModelInter> StationModellist=reportModelInterMapper.selectReReportModel(reportStatus);
        System.out.println(StationModellist);
        //处理场站模板 接口 生成报告
        if (StationModellist!=null && StationModellist.size()>0) {
            for (ReportModelInter obj : StationModellist) {
                //更新报告管理的状态为 在处理
                if (obj == null) {
                    continue;
                }
                QueryWrapper<ReportMng> qw=new QueryWrapper<>();
                qw.eq("id",obj.getId());
                ReportMng mng=reportMngMapper.selectOne(qw);
                //ReportMng mng = new ReportMng();
               // mng.setId(obj.getId());
                mng.setReportStatus(3);
                reportMngMapper.updateById(mng);
                int i = reportMngService.updateReport(obj, mng);
            }
        }

    }
}
