package com.bonc.jibei.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonc.jibei.config.WordCfgProperties;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.entity.ReportModel;
import com.bonc.jibei.entity.StationModelRel;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.mapper.ReportModelMapper;
import com.bonc.jibei.mapper.StationModelRelMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.util.HttpUtil;
import com.bonc.jibei.util.JsonUtil;
import com.bonc.jibei.vo.ReportMngList;
import com.bonc.jibei.vo.ReportModelInter;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 16:17
 * @Description: TODO
 */
@Service
public class ReportMngServiceImpl extends ServiceImpl<ReportMngMapper,ReportMng> implements ReportMngService {
    @Resource
    private WordCfgProperties wordCfgProperties;

    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    @Resource
    private ReportMngMapper reportMngMapper;
    @Resource
    private ReportModelMapper reportModelMapper;

    @Resource
    private StationModelRelMapper stationModelRelMapper;

    @Override
    public List<ReportMngList> reportMngList(IPage<ReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus, Long start, Long size) {

        return reportMngMapper.selectReportMngList(page,stationName,year,quarter,stationType,reportStatus, start, size);
    }

    @Override
    public List<String> urlList(List<Integer> ids) {
        return reportMngMapper.selectDocUrl(ids);
    }

    @Override
    public int insertReport(ReportModelInter obj) {
        String interUrl=wordCfgProperties.getInterfaceUrl();

        HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
        Configure config = Configure.builder().bind("list", policy).build();
        List<Map> list= Lists.newArrayList();
        Map data = new HashMap<>();

        //本季度 开始时间和结束时间
        String startdate= DateUtil.getDateQrt(true).toString();
        String enddate=DateUtil.getDateQrt(false).toString();


        //Map data1 = new HashMap<>();
        //JSONObject jsonstr= JsonUtil.createJson(obj.getStationId(),obj.getStationType(),startdate+" 00:00:00",enddate+" 23:59:59");
        //JSONObject json1= HttpUtil.httpPost(interUrl+obj.getInterUrl(),jsonstr);
        //场站模板所有接口
        List<ReportModelInter> listInter=reportModelInterMapper.selectReportModelInter(obj.getStationType(),obj.getStationId());
        for (ReportModelInter iobj:listInter ){
            //调用接口

            ReportMng mng=new ReportMng();
            mng.setReportStatus(0);//待审核
            mng.setCreateTime(LocalDateTime.now());
            mng.setModelVersion(obj.getModelVersion());
            mng.setReportName(obj.getReportName());
            mng.setStationId(obj.getStationId());
            mng.setStationType(obj.getStationType());
            mng.setReportYear(DateUtil.lastQrtYear());//年份
            mng.setReportQuarter(DateUtil.lastQrt());// 上一季度
            reportMngMapper.insert(mng);
        }
        return 0;
    }
    @Override
    public int updateReport(ReportModelInter reportModelInter,ReportMng reportMng) {
        Integer stationType=reportMng.getStationType();//场站类型
        Integer stationId=reportMng.getStationId();//场站ID
        Integer year=reportMng.getReportYear();//年份
        Integer quarty=reportMng.getReportQuarter();//季度

        QueryWrapper<StationModelRel> qw=new QueryWrapper<>();
        qw.eq("station_id",reportMng.getStationId());
        qw.eq("station_type",reportMng.getStationType());
        StationModelRel rel=stationModelRelMapper.selectOne(qw);
        Integer modelId=null;
        if (rel==null){
            return 0;
        }
        modelId=rel.getModelId();
        QueryWrapper<ReportModel> reportqw=new QueryWrapper<>();
        reportqw.eq("id",modelId);
        ReportModel reportModel=reportModelMapper.selectById(reportqw);
        if (reportModel==null){
            return 0;
        }
        String modelUrl=reportModel.getModelFileUrl();//模板
        if (StrUtil.isBlank(modelUrl)){
            return 0;
        }
        //场站模板所有接口
        List<ReportModelInter> listInter=reportModelInterMapper.selectReReportModelInter(reportMng.getStationType(),reportMng.getStationId());
        for (ReportModelInter obj:listInter ){

            //调用接口
            String reportUrl="";
            reportMng.setReportUrl(reportUrl);
            reportMng.setReportStatus(0);//重置待审核状态
            return reportMngMapper.updateById(reportMng);
        }
        return 1;
    }
}
