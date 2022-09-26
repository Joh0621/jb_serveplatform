package com.bonc.jibei.controller;


import com.bonc.jibei.api.Result;
import com.bonc.jibei.mapper.NumericalStatisticsMapper;
import com.bonc.jibei.service.NumericalStatisticsService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.vo.NumericalStatisticsVo;
import com.bonc.jibei.vo.RadiationDoseDistributedVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

     * @return
     */
    @RequestMapping("monitoringAnalysis")
    @ResponseBody
    public Result monitoringAnalysis(String year,String flag1) {
       Map<String,Object> result=numericalStatisticsService.monitoringAnalysis( year,"",flag1);
        return Result.ok(result);
    }

    /**
     * 水平总辐射量趋势对比分析
//  flag1 控制外层 falg2 控制内层
     * @return
     */
    @RequestMapping("monitoringAnalysisDq")
    @ResponseBody
    public Result monitoringAnalysisDq(String year,String flag1,String flag2) {
        if (flag1==null||"".equals(flag1)) {
            flag1="1";
        }
        List<NumericalStatisticsVo> numericalStatisticsVos = numericalStatisticsMapper.selMonitoringAnalysisDq(year,flag1);
//        List<String> dates = numericalStatisticsVos.stream().distinct().map(NumericalStatisticsVo::getDataTime).collect(Collectors.toList());
        List<String> xList = new ArrayList<>();
        List<Double> jbList = new ArrayList<>();
        List<Double> zjkList = new ArrayList<>();
        List<Double> tsList = new ArrayList<>();
        List<Double> lfList = new ArrayList<>();
        List<Double> qhdList = new ArrayList<>();
        List<Double> cdList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Double> collect = null;
        if (flag1!=null&&flag1!="1"){
            if ("2".equals(flag1)){
                if ("1".equals(flag2)){
                    collect= numericalStatisticsVos.stream().collect(
                            Collectors.toMap(k->k.getDataTime()+"-"+k.getAName(), NumericalStatisticsVo::getAvgTemp));
                }else if ("2".equals(flag2)){
                    collect= numericalStatisticsVos.stream().collect(
                            Collectors.toMap(k->k.getDataTime()+"-"+k.getAName(), NumericalStatisticsVo::getMaxTemp));
                }else if ("3".equals(flag2)){
                    collect= numericalStatisticsVos.stream().collect(
                            Collectors.toMap(k->k.getDataTime()+"-"+k.getAName(), NumericalStatisticsVo::getMinTemp));
                }
            }else if ("3".equals(flag1)){
                if ("1".equals(flag2)){
                    collect= numericalStatisticsVos.stream().collect(
                            Collectors.toMap(k->k.getDataTime()+"-"+k.getAName(), NumericalStatisticsVo::getAvgWindSpeed));
                }else if ("2".equals(flag2)){
                    collect= numericalStatisticsVos.stream().collect(
                            Collectors.toMap(k->k.getDataTime()+"-"+k.getAName(), NumericalStatisticsVo::getMaxWindSpeed));
                }else if ("3".equals(flag2)){
                    collect= numericalStatisticsVos.stream().collect(
                            Collectors.toMap(k->k.getDataTime()+"-"+k.getAName(), NumericalStatisticsVo::getMinWindSpeed));
                }
            }else {
                collect= numericalStatisticsVos.stream().collect(
                        Collectors.toMap(k->k.getDataTime()+"-"+k.getAName(), NumericalStatisticsVo::getRadiationDose));
            }
        }

    if (collect==null){
        return Result.ok(map);
    }
        //所有时间
//        List<String> dates = numericalStatisticsVos.stream().map(NumericalStatisticsVo::getDataTime).collect(Collectors.toList());
        List<String> dates=   DateUtil.getMonthOfYear(year);
//        List<String> dates = numericalStatisticsVos.stream().distinct().collect(Collectors.toList());
//        List<String> dates = Arrays.asList("05", "06", "07", "08", "09");
        xList=dates;
        System.out.println(dates);
        List<String> dqList = Arrays.asList("-张家口", "-唐山", "-廊坊", "-秦皇岛", "-承德", "-冀北");
        //组合时间和地区集合
        List<String> resultList13 = dates.stream().flatMap(str -> dqList.stream().map(str::concat))
                .collect(Collectors.toList());

        for (String x: resultList13) {


                if (collect.get(x) == null) {
                    String[] split = x.split("-");
                    switch (split[1]) {
                        case "张家口":
                            System.out.println(x);
                            zjkList.add(null);
                            break;
                        case "唐山":
                            tsList.add(null);
                            break;
                        case "廊坊":
                            lfList.add(null);
                            break;
                        case "秦皇岛":
                            qhdList.add(null);
                            break;
                        case "承德":
                            cdList.add(null);
                            break;
                        case "冀北":
                            jbList.add(null);
                            break;
                    }
                } else {
                    String[] split = x.split("-");

                    switch (split[1]) {
                        case "张家口":
                            zjkList.add(collect.get(x));
                            break;
                        case "唐山":
                            tsList.add(collect.get(x));
                            break;
                        case "廊坊":
                            lfList.add(collect.get(x));
                            break;
                        case "秦皇岛":
                            qhdList.add(collect.get(x));
                            break;
                        case "承德":
                            cdList.add(collect.get(x));
                            break;
                        case "冀北":
                            jbList.add(collect.get(x));
                            break;
                    }
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
    public Result radiationDoseDistributed(String year,String flag1) {
        if (flag1==null||"".equals(flag1)) {
            flag1="1";
        }
        Map<String,Object> result=numericalStatisticsService.radiationDoseDistributed( year,"",flag1);
        return Result.ok(result);
    }

    /**
     * 水平辐照度概率分布-地区
     */
    @RequestMapping("radiationDoseDistributedDq")
    @ResponseBody
    public Result radiationDoseDistributedDq(String year ,String flag1,String flag) {
        if (flag1==null||"".equals(flag1)) {
            flag1="1";
        }
        if (flag1=="1"){
            flag="1";
        }
        List<RadiationDoseDistributedVo> numericalStatisticsVos = numericalStatisticsMapper.selRadiationDoseDistributed( year,flag,flag1);
        List<String> xList = new ArrayList<>();
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
        Map<String, Double> collectCnt = numericalStatisticsVos.stream().collect(
                Collectors.toMap(k -> k.getValue() + "-" + k.getAName(), RadiationDoseDistributedVo::getCnt));
        Map<String, Double> collectRate= numericalStatisticsVos.stream().collect(
                Collectors.toMap(k->k.getValue()+"-"+k.getAName(), RadiationDoseDistributedVo::getRate));
        List<String> dates = numericalStatisticsVos.stream().distinct().map(RadiationDoseDistributedVo::getValue).collect(Collectors.toList());
        List<String> datesValue = dates.stream().distinct().collect(Collectors.toList());
        xList=datesValue;
        //地区集合
        List<String> dqList = Arrays.asList("-张家口", "-唐山", "-廊坊", "-秦皇岛", "-承德", "-冀北");
        //组合时间和地区集合
        List<String> resultList13 = xList.stream().flatMap(str -> dqList.stream().map(str::concat))
                .collect(Collectors.toList());
        System.out.println(resultList13);
        for (String x: resultList13) {
            if (collectCnt.get(x) == null) {
                String[] split = x.split("-");
                switch (split[1]) {
                    case "张家口":
                        zjkDayList.add(null);
                        break;
                    case "唐山":
                        tsDayList.add(null);
                        break;
                    case "廊坊":
                        lfDayList.add(null);
                        break;
                    case "秦皇岛":
                        qhdDayList.add(null);
                        break;
                    case "承德":
                        cdDayList.add(null);
                        break;
                    case "冀北":
                        jbList.add(null);
                        break;
                }
            } else {
                String[] split = x.split("-");

                switch (split[1]) {
                    case "张家口":
                        zjkDayList.add(collectCnt.get(x));
                        break;
                    case "唐山":
                        tsDayList.add(collectCnt.get(x));
                        break;
                    case "廊坊":
                        lfDayList.add(collectCnt.get(x));
                        break;
                    case "秦皇岛":
                        qhdDayList.add(collectCnt.get(x));
                        break;
                    case "承德":
                        cdDayList.add(collectCnt.get(x));
                        break;
                    case "冀北":
                        jbList.add(collectCnt.get(x));
                        break;
                }
            }
            if (collectRate.get(x) == null) {
                String[] split = x.split("-");
                switch (split[1]) {
                    case "张家口":
                        System.out.println(x);
                        zjkRateList.add(null);
                        break;
                    case "唐山":
                        tsRateList.add(null);
                        break;
                    case "廊坊":
                        lfRateList.add(null);
                        break;
                    case "秦皇岛":
                        qhdRateList.add(null);
                        break;
                    case "承德":
                        cdRateList.add(null);
                        break;
                    case "冀北":
                        jbList.add(null);
                        break;
                }

            } else {
                String[] split = x.split("-");

                switch (split[1]) {
                    case "张家口":
                        zjkRateList.add(collectRate.get(x));
                        break;
                    case "唐山":
                        tsRateList.add(collectRate.get(x));
                        break;
                    case "廊坊":
                        lfRateList.add(collectRate.get(x));
                        break;
                    case "秦皇岛":
                        qhdRateList.add(collectRate.get(x));
                        break;
                    case "承德":
                        cdRateList.add(collectRate.get(x));
                        break;
                }
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
    public Result selSunHoursTrend(String year) {
        Map<String,Object> result=numericalStatisticsService.selSunHoursTrend( year);
        return Result.ok(result);
    }


}
