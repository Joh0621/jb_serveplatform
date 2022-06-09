package com.bonc.jibei.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.entity.StationModelRel;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.mapper.StationModelRelMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.service.ReportService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.util.JsonUtil;
import com.bonc.jibei.vo.ReportMngList;
import com.bonc.jibei.vo.ReportModelInter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 16:17
 * @Description: TODO
 */
@Service
public class ReportMngServiceImpl extends ServiceImpl<ReportMngMapper, ReportMng> implements ReportMngService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportMngServiceImpl.class);

    @Resource
    private ReportMngMapper reportMngMapper;

    @Resource
    private StationModelRelMapper stationModelRelMapper;

    @Resource
    private ReportService reportService;

    @Override
    public List<ReportMngList> reportMngList(IPage<ReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus) {

        return reportMngMapper.selectReportMngList(page, stationName, year, quarter, stationType, reportStatus);
    }

    @Override
    public List<String> urlList(List<Integer> ids) {
        return reportMngMapper.selectDocUrl(ids);
    }

    /**
     *  新生成报告文件
     * @param reportMng
     * @return
     * @throws IOException
     */
    @Override
    @Async("threadPoolTaskExecutor")
    public int insertReport(ReportModelInter reportMng) throws IOException {
        //取得场站类型的模板
        /**
        QueryWrapper<StationModelRel> qw = new QueryWrapper<>();
        qw.eq("station_id", reportMng.getStationId());
        List<StationModelRel> rell = stationModelRelMapper.selectList(qw);
        Integer modelId = null;
        if (rell == null || rell.get(0) == null) {
            return 0;
        }
        modelId = rell.get(0).getModelId();
        **/
        Integer modelId =reportMng.getModelId();
        //上季度 开始时间和结束时间
        String startdate = DateUtil.lastQrtStart();
        String enddate = DateUtil.lastQrtEnd();
        JSONObject jsonstr = JsonUtil.createJson(reportMng.getStationId(), reportMng.getStationType(), modelId, startdate + " 00:00:00", enddate + " 23:59:59");
        //调用接口 生成报告文件
        String reportUrl = reportService.generate(jsonstr);

        //生成报告
        ReportMng mng = new ReportMng();
        mng.setReportStatus(0);//待审核
        mng.setReportUrl(reportUrl);//生成的报告文件
        mng.setCreateTime(LocalDateTime.now());
        mng.setReportTime(LocalDateTime.now());//报告生成时间
        mng.setModelVersion(reportMng.getModelVersion());
        mng.setReportName(reportMng.getReportName());
        mng.setStationId(reportMng.getStationId());
        mng.setStationType(reportMng.getStationType());
        mng.setReportYear(DateUtil.lastQrtYear());//年份
        mng.setReportQuarter(DateUtil.lastQrt());// 上一季度
        return reportMngMapper.insert(mng);
    }

    /**
     * 重新生成报告文件
     * @param reportMng
     * @throws IOException
     */
    @Override
    @Async("threadPoolTaskExecutor")
    public void updateReport(ReportMng reportMng) throws IOException {
        //取得场站类型的模板
        /**
        QueryWrapper<StationModelRel> qw = new QueryWrapper<>();
        qw.eq("station_id", reportMng.getStationId());
        List<StationModelRel> rell = stationModelRelMapper.selectList(qw);
        Integer modelId = null;
        if (rell == null || rell.get(0) == null) {
            return;
        }
        modelId = rell.get(0).getModelId();
         **/
        Integer modelId =reportMng.getModelId();
        //获得已知季度的 开始时间和结束时间
        Map<String, String> d = DateUtil.getStartByYearQrt(reportMng.getReportYear(), reportMng.getReportQuarter());
        JSONObject jsonstr = JsonUtil.createJson(reportMng.getStationId(), reportMng.getStationType(), modelId, d.get("startDate"), d.get("endDate"));
        //调用接口 生成报告文件
        LOGGER.info("generate report : " + reportMng.getStationId());
//        LOGGER.info("jsonstr : " + jsonstr);
        String reportUrl = reportService.generate(jsonstr);
        reportMng.setReportUrl(reportUrl);
        reportMng.setReportStatus(0);//重置待审核状态
        reportMng.setReportTime(LocalDateTime.now());//报告生成时间
        reportMngMapper.updateById(reportMng);
    }
}
