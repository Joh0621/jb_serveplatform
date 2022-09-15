package com.bonc.jibei.controller;


import com.bonc.jibei.api.ErrorTypeEnum;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.mapper.NumericalStatisticsMapper;
import com.bonc.jibei.service.NumericalStatisticsService;
import com.bonc.jibei.vo.NumericalStatisticsVo;
import com.bonc.jibei.vo.RadiationDoseDistributedVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数值统计数据
 */
@RequestMapping("NumericalStatistics")
@RestController
public class NumericalStatisticsController {
    @Resource
    private NumericalStatisticsService numericalStatisticsService;

    @Resource
    private NumericalStatisticsMapper numericalStatisticsMapper;

    /**
     * 监测分析-趋势分析
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("monitoringAnalysis")
    @ResponseBody
    public Result monitoringAnalysis(String startTime, String endTime,String flag1) {
       Map<String,Object> result=numericalStatisticsService.monitoringAnalysis( startTime, endTime,"",flag1);
        return Result.ok(result);
    }

    /**
     * 水平总辐射量趋势对比分析
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("monitoringAnalysisDq")
    @ResponseBody
    public Result monitoringAnalysisDq(String startTime, String endTime,String flag1,String flag2) {
        if (flag1==null||"".equals(flag1)) {
            flag1="1";
        }
        List<NumericalStatisticsVo> numericalStatisticsVos = numericalStatisticsMapper.selMonitoringAnalysis(startTime, endTime,"1",flag1);
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> jbList = new ArrayList<>();
        ArrayList<Object> zjkList = new ArrayList<>();
        ArrayList<Object> tsList = new ArrayList<>();
        ArrayList<Object> lfList = new ArrayList<>();
        ArrayList<Object> qhdList = new ArrayList<>();
        ArrayList<Object> cdList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (NumericalStatisticsVo vo :numericalStatisticsVos){
            if (flag1!=null&&flag1!="1"){
                if ("2".equals(flag1)){
                    if ("1".equals(flag2)){
                        vo.setRadiationDose(vo.getAvgTemp());
                    }else if ("2".equals(flag2)){
                        vo.setRadiationDose(vo.getMaxTemp());
                    }else if ("3".equals(flag2)){
                        vo.setRadiationDose(vo.getMinTemp());
                    }
                }else if ("3".equals(flag1)){
                    if ("1".equals(flag2)){
                        vo.setRadiationDose(vo.getAvgWindSpeed());
                    }else if ("2".equals(flag2)){
                        vo.setRadiationDose(vo.getMaxWindSpeed());
                    }else if ("3".equals(flag2)){
                        vo.setRadiationDose(vo.getMinWindSpeed());
                    }
                }
            }
            switch (vo.getAName()) {
                case "张家口":
                    zjkList.add(vo.getRadiationDose());
                    xList.add(vo.getYearMonthDate());
                    break;
                case "唐山":
                    tsList.add(vo.getRadiationDose());
                    break;
                case "廊坊":
                    lfList.add(vo.getRadiationDose());
                    break;
                case "秦皇岛":
                    qhdList.add(vo.getRadiationDose());
                    break;
                case "承德":
                    cdList.add(vo.getRadiationDose());
                    break;
                case "冀北":
                    jbList.add(vo.getRadiationDose());
                    break;
            }
        }
        map.put("xList",xList);
        map.put("张家口",zjkList);
        map.put("唐山",tsList);
        map.put("廊坊",lfList);
        map.put("秦皇岛",qhdList);
        map.put("承德",cdList);
        map.put("冀北",jbList);
        return Result.ok(map);
    }
    /**
     * 水平辐照度概率分布
     */
    @RequestMapping("radiationDoseDistributed")
    @ResponseBody
    public Result radiationDoseDistributed(String startTime, String endTime,String flag1) {
        if (flag1==null||"".equals(flag1)) {
            flag1="1";
        }
        Map<String,Object> result=numericalStatisticsService.radiationDoseDistributed( startTime, endTime,"",flag1);
        return Result.ok(result);
    }

    /**
     * 水平辐照度概率分布-地区
     */
    @RequestMapping("radiationDoseDistributedDq")
    @ResponseBody
    public Result radiationDoseDistributedDq(String startTime, String endTime,String flag1) {
        if (flag1==null||"".equals(flag1)) {
            flag1="1";
        }
        List<RadiationDoseDistributedVo> numericalStatisticsVos = numericalStatisticsMapper.selRadiationDoseDistributed( startTime, endTime,"1",flag1);
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> jbList = new ArrayList<>();
        ArrayList<Object> zjkDayList = new ArrayList<>();
        ArrayList<Object> tsDayList = new ArrayList<>();
        ArrayList<Object> lfDayList = new ArrayList<>();
        ArrayList<Object> qhdDayList = new ArrayList<>();
        ArrayList<Object> cdDayList = new ArrayList<>();
        ArrayList<Object> zjkRateList = new ArrayList<>();
        ArrayList<Object> tsRateList = new ArrayList<>();
        ArrayList<Object> lfRateList = new ArrayList<>();
        ArrayList<Object> qhdRateList = new ArrayList<>();
        ArrayList<Object> cdRateList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (RadiationDoseDistributedVo vo :numericalStatisticsVos){
            switch (vo.getAName()) {
                case "张家口":
                    zjkDayList.add(vo.getCnt());
                    zjkRateList.add(vo.getRate());
                    xList.add(vo.getValue());
                    break;
                case "唐山":
                    tsDayList.add(vo.getCnt());
                    tsRateList.add(vo.getRate());
                    break;
                case "廊坊":
                    lfDayList.add(vo.getCnt());
                    lfRateList.add(vo.getRate());
                    break;
                case "秦皇岛":
                    qhdDayList.add(vo.getCnt());
                    qhdRateList.add(vo.getRate());
                    break;
                case "承德":
                    cdDayList.add(vo.getCnt());
                    cdRateList.add(vo.getRate());
                    break;
                case "冀北":
                    jbList.add(vo.getCnt());
                    break;
            }
        }
        map.put("xList",xList);
        map.put("冀北天数",jbList);
        map.put("张家口天数",zjkDayList);
        map.put("唐山天数",tsDayList);
        map.put("廊坊天数",lfDayList);
        map.put("秦皇岛天数",qhdDayList);
        map.put("承德天数",cdDayList);
        map.put("张家口占比",zjkRateList);
        map.put("唐山占比",tsRateList);
        map.put("廊坊占比",lfRateList);
        map.put("秦皇岛占比",qhdRateList);
        map.put("承德占比",cdRateList);
        return Result.ok(map);
    }
    /**
     * 日照时数趋势对比分析
     */
    @RequestMapping("selSunHoursTrend")
    @ResponseBody
    public Result selSunHoursTrend(String startTime, String endTime) {
        Map<String,Object> result=numericalStatisticsService.selSunHoursTrend( startTime, endTime);
        return Result.ok(result);
    }


}
