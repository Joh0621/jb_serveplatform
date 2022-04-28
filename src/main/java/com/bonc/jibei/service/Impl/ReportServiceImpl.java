package com.bonc.jibei.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonc.jibei.config.WordCfgProperties;
import com.bonc.jibei.entity.Report;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.mapper.ReportMapper;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.service.ReportService;
import com.bonc.jibei.util.DateUtil;

import com.bonc.jibei.util.HttpUtil;
import com.bonc.jibei.util.JsonUtil;
import com.bonc.jibei.vo.ReportModelInter;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/25 14:09
 * @Description: TODO
 */
@Service
public class ReportServiceImpl  extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Resource
    private WordCfgProperties wordCfgProperties;

    @Resource
    private ReportMngMapper reportMngMapper;
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


            Map data1 = new HashMap<>();
            JSONObject jsonstr= JsonUtil.createJson(obj.getStationId(),obj.getStationType(),startdate+" 00:00:00",enddate+" 23:59:59");
            JSONObject json1= HttpUtil.httpPost(interUrl+obj.getInterUrl(),jsonstr);

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
       // }
        return 0;
    }
    @Override
    public int updateReport(ReportModelInter reportlist) {


        String reportUrl="";
        QueryWrapper<ReportMng> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",reportlist.getInterUrl());
        ReportMng mng=reportMngMapper.selectById(queryWrapper);
        mng.setReportUrl(reportUrl);
        mng.setReportStatus(0);//重置待审核状态
        return reportMngMapper.updateById(mng);
    }
}
