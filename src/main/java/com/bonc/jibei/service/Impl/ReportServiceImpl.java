package com.bonc.jibei.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonc.jibei.config.WordCfgProperties;
import com.bonc.jibei.entity.Report;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.mapper.ReportMapper;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.service.ReportService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.util.EchartsToPicUtil;
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
//List<ReportModelInter> reportlist
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

       // for (ReportModelInter obj:reportlist){
            Map data1 = new HashMap<>();
            JSONObject jsonstr= JsonUtil.createJson(obj.getStationId(),obj.getStationType(),startdate+" 00:00:00",enddate+" 23:59:59");
            JSONObject json1= HttpUtil.httpPost(interUrl+obj.getInterUrl(),jsonstr);
            //接口类型,1:数据 2：列表；3：柱状图，4：折线图，5：雷达图
            if (obj.getStationType()==1){

                if (json1!=null){
                    JSONArray arr=json1.getJSONArray("data");
                    if (arr.size()>0){
                        JSONObject arr1=arr.getJSONObject(0);
                        arr1.forEach((key, value) -> {
                            data.put(key,value);
                        });
                    }
                }
                //continue;
            }
            //列表
            if (obj.getStationType()==2){
                JSONArray arr=json1.getJSONArray("data");
                for (int i = 0; i < arr.size(); i++) {
                    Map datax = new HashMap<>();//通过map存放要填充的数据
                    JSONObject jsonObject= JSON.parseObject(arr.get(i).toString());
                    jsonObject.forEach((key, value) -> {
                        datax.put(key,value);
                    });
                    list.add(datax);
                }
                if (list.size()>0){
                    data.put(obj.getPlaceTag(),list);
                }
            }
            //柱状图
            if (obj.getStationType()==3){
                JSONArray arr=json1.getJSONArray("data");
                /**
                 jsonStr = "{\"success\":true," +
                 " \"message\":\"ok\", " +
                 " \"code\":\"0\", " +
                 "\"data\":[" +
                 "" +
                 "" +
                 "{title:'','name':'mmm',age:12}" +
                 "]}";
                 **/
                if (arr.size()>0){
                    JSONObject arr1=arr.getJSONObject(0);
                    String[] key1=new String[arr1.size()];
                    String[] val1=new String[arr1.size()];
                    int i=0;
                    for (String keyx:arr1.keySet()){
                        key1[i]=keyx;
                        val1[i]=arr1.get(keyx).toString();
                    }
                }
                String pathFile= EchartsToPicUtil.generateEChart(EchartsToPicUtil.echartBar(true,wordCfgProperties.getPngPath(),wordCfgProperties.getOSType()),wordCfgProperties.getPngPath(), wordCfgProperties.getOSType(),"");
            }
            //折线图
            if (obj.getStationType()==4){

            }
            //雷达图
            if (obj.getStationType()==5){

            }
            ReportMng mng=new ReportMng();
            mng.setReportStatus(0);//待审核
            mng.setCreateTime(LocalDateTime.now());
            mng.setModelVersion(obj.getModelVersion());
            mng.setReportName(obj.getReportName());
            mng.setStationId(obj.getStationId());
            mng.setStationType(obj.getStationType());
            //mng.setReportYear(obj.);
            mng.setReportQuarter(0);
            reportMngMapper.insert(mng);
       // }

        return 0;
    }

    @Override
    public int updateReport(ReportModelInter reportlist) {
        return 0;
    }
}
