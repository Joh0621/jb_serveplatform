package com.bonc.jibei.controller;


import com.alibaba.fastjson2.JSON;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.mapper.PerformanceAnalysisMapper;
import com.bonc.jibei.vo.DeviceModelVo;
import com.bonc.jibei.vo.UseOfHoursVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("performanceAnalysis")
@RestController
public class PerformanceAnalysisController {
    @Resource
    private PerformanceAnalysisMapper performanceAnalysisMapper;

    /**
     * 等效利用小时数趋势
     * @param Year
     * @return
     */
    @RequestMapping("uesOfHoursTrend")
    @ResponseBody
    public Result uesOfHoursTrend( String Year) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.seluesOfHoursTrend(Year);

        Map<String, Object> map = new HashMap<>();
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> yList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){

         xList.add(vo.getXData());
         yList.add(vo.getYData());
        }
        map.put("xData",xList);
        map.put("yData",yList);
        return Result.ok(map);
    }



    /**
     * 发电性能指标
     * @param Year
     * @return
     */
    @RequestMapping("powerGenerationIndex")
    @ResponseBody
    public Result powerGenerationIndex( String Year) {
        Map<String, Object> map = new HashMap<>();
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> yList = new ArrayList<>();

        map.put("等效利用小时数",98.2);
        map.put("平均故障间隔时间",1000);
        map.put("发电能效PR",85.4);
        map.put("故障次数",1100);
        map.put("故障检修时间",2000);
        map.put("功率预测准确度",98);
        return Result.ok(map);
    }
    /**
     * 等效利用小时数趋势-地区
     * @param Year
     * @return
     */
    @RequestMapping("uesOfHoursTrendDq")
    @ResponseBody
    public Result uesOfHoursTrendDq( String Year) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.seluesOfHoursTrendDq(Year);

        Map<String, Object> map = new HashMap<>();
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> jbList = new ArrayList<>();
        ArrayList<Object> zjkList = new ArrayList<>();
        ArrayList<Object> tsList = new ArrayList<>();
        ArrayList<Object> lfList = new ArrayList<>();
        ArrayList<Object> qhdList = new ArrayList<>();
        ArrayList<Object> cdList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            switch (vo.getYData1()) {
                case "张家口":
                    zjkList.add(vo.getYData());
                    xList.add(vo.getXData());
                    break;
                case "唐山":
                    tsList.add(vo.getYData());
                    break;
                case "廊坊":
                    lfList.add(vo.getYData());
                    break;
                case "秦皇岛":
                    qhdList.add(vo.getYData());
                    break;
                case "承德":
                    cdList.add(vo.getYData());
                    break;
                case "冀北":
                    jbList.add(vo.getYData());
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
     * 等效利用小时数趋势-断面
     * @param Year
     * @return
     */
    @RequestMapping("uesOfHoursTrendDm")
    @ResponseBody
    public Result uesOfHoursTrendDm( String Year) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.seluesOfHoursTrendDm(Year);

        Map<String, Object> map = new HashMap<>();
        List<String> yData1List=useOfHoursVos.stream().map(UseOfHoursVo::getYData1).distinct().collect(Collectors.toList());
        List<String> xList = useOfHoursVos.stream().map(UseOfHoursVo::getXData).distinct().collect(Collectors.toList());

        //                Collectors.collectingAndThen(Collectors.toList(),value->value.get(0))));
        Map<String, List<String>> mapd = new HashMap<>();
        //根据名称分组，并获取ydata数据
        for (UseOfHoursVo useOfHoursVo : useOfHoursVos) {
            mapd.computeIfAbsent(useOfHoursVo.getYData1(), k -> new ArrayList<>()).add(useOfHoursVo.getYData());
        }
        map.put("xList",xList);
        for (String  s:yData1List
        ) {
            map.put(s,mapd.get(s));
        }
        return Result.ok(map);
    }

    /**
     * 等效利用小时数场站排名
     * @param Year
     * @param sortType 正序排列 0 ，倒序排列 1
     * @return
     */
    @RequestMapping("uesOfHoursStationTop10")
    @ResponseBody
    public Result selUesOfHoursStationTop10( String Year,String sortType) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.selUesOfHoursStationTop10(Year,sortType);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("name",vo.getYData());
            map.put("value",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 等效利用小时数场站排名
     * @param Year
     * @return
     */
    @RequestMapping("uesOfHoursCompanyTop10")
    @ResponseBody
    public Result selUesOfHoursCompanyTop10( String Year,String sortType) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.selUesOfHoursCompanyTop10(Year,sortType);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("name",vo.getYData());
            map.put("value",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 发电能效PR趋势
     * @param Year
     * @return
     */
    @RequestMapping("prTrend")
    @ResponseBody
    public Result selPrTrend( String Year) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.selPrTrend(Year);

        Map<String, Object> map = new HashMap<>();
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> yList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            xList.add(vo.getXData());
            yList.add(vo.getYData());
        }
        map.put("xData",xList);
        map.put("yData",yList);
        return Result.ok(map);
    }

    /**
     * 发电能效PR趋势地区
     * @param Year
     * @return
     */
    @RequestMapping("prTrendDq")
    @ResponseBody
    public Result prTrendDq( String Year) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.selPrTrendDq(Year);
        Map<String, Object> stringObjectMap = performanceAnalysisMapper.selPrTrendDqValue(Year);
        List<String> Names = performanceAnalysisMapper.selPrTrendDqName(Year);
        Map<String, Object> result = selTrend(useOfHoursVos);
        result.put("max",stringObjectMap.get("max"));
        result.put("min",stringObjectMap.get("min"));
        result.put("avg",stringObjectMap.get("avg"));
        result.put("maxName",Names.get(0));
        result.put("minName",Names.get(1));
        return Result.ok(result);
    }

    /**
     * 发电能效PR-场站排名
     * @param Year
     * @return
     */
    @RequestMapping("prStationTop10")
    @ResponseBody
    public Result prStationTop10( String Year,String sortType) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.prStationTop10(Year,sortType);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("name",vo.getYData());
            map.put("value",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }
    /**
     * 发电能效PR-集团排名
     * @param Year
     * @return
     */
    @RequestMapping("prCompanyTop10")
    @ResponseBody
    public Result prCompanyTop10( String Year,String sortType) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.prCompanyTop10(Year,sortType);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("name",vo.getYData());
            map.put("value",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 功率预测准确度趋势
     */
    @RequestMapping("AccuracyRateTrend")
    @ResponseBody
    public Result selAccuracyRateTrend( String Year) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.selAccuracyRateTrend(Year);

        Map<String, Object> map = new HashMap<>();
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> yList = new ArrayList<>();
        ArrayList<Object> yList1 = new ArrayList<>();
        ArrayList<Object> yList2 = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            xList.add(vo.getXData());
            yList.add(vo.getYData());
            yList1.add(vo.getYData1());
            yList2.add(vo.getYData2());
        }
        map.put("xData",xList);
        map.put("yData",yList);
        map.put("yData1",yList1);
        map.put("yData2",yList2);
        return Result.ok(map);
    }

    /**
     * 各厂家功率预测预测准确率对比
     */
    @RequestMapping("AccuracyRateCompany")
    @ResponseBody
    public Result AccuracyRateCompany( String startTime,String endTime) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.AccuracyRateCompany(startTime,endTime);
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> yList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            xList.add(vo.getXData());
            yList.add(vo.getYData());
        }
        map.put("xData",xList);
        map.put("yData",yList);

        return Result.ok(map);
    }


    /**
     * 功率预测准确率排名
     * @param Year
     *  @param type 1 断面 2：场站 3：集团
     * @return
     */
    @RequestMapping("AccuracyRateTop10")
    @ResponseBody
    public Result AccuracyRateTop10( String Year,String type,String sortType) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.AccuracyRateTop10(Year,type,sortType);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("name",vo.getYData());
            map.put("value",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 故障分析
     * @param Year
     * @return
     */
    @RequestMapping("faultInfoType")
    @ResponseBody
    public Result faultInfoType( String Year) {
        LinkedHashMap<String, Object> map = performanceAnalysisMapper.faultInfoType(Year);
        ArrayList<Object> yList1 = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        //数值集合
        String json= JSON.toJSONString(map);
        ArrayList<Double> yList = new ArrayList(map.values());
        ArrayList<Object> xList = new ArrayList(map.keySet());
        //求和得总数
        Double sum = yList.stream().reduce(Double::sum).orElse(0.00);
        for (Double s : yList) {
            if (s!=null){
                DecimalFormat df = new DecimalFormat("0.00");
                df.setRoundingMode(RoundingMode.HALF_UP);
                yList1.add(df.format(s/sum*100));
            }
        }
        resultMap.put("xList",xList);
        resultMap.put("yList",yList);
        resultMap.put("yList1",yList1);
        return Result.ok(resultMap);
    }

    /**
     * 地区场站运行状态分布
     * @param YearMonth
     * @return
     */
    @RequestMapping("runningStatus")
    @ResponseBody
    public Result runningStatus( String YearMonth) {
        LinkedHashMap<String, Double> map = performanceAnalysisMapper.runningStatus(YearMonth);
        ArrayList<Object> resultList = new ArrayList<>();
        //数值集合
        ArrayList<Double> yList = new ArrayList(map.values());
        ArrayList<Object> xList = new ArrayList(map.keySet());
        xList.add("总和");
        //求和得总数
        Double sum = yList.stream().reduce(Double::sum).orElse(0.00);
        yList.add(sum);
            for (int i=0;i<yList.size();i++){
            if (yList.get(i)!=null){
                Map<String, Object> xMap = new HashMap<>();
                DecimalFormat df = new DecimalFormat("0.00");
                df.setRoundingMode(RoundingMode.HALF_UP);
                xMap.put("name",xList.get(i).toString());
                xMap.put("value",df.format(yList.get(i)/sum*100));
                resultList.add(xMap);
            }
        }
        return Result.ok(resultList);
    }


    /**
     * 地区逆变器故障多维分析
     * @param yearMonth
       @param dataType 1:故障时长 2:故障次数
     *  @param type 1 断面 2：类型 3：集团 4:厂家
     * @return
     */
    @RequestMapping("faultAnalyze")
    @ResponseBody
    public Result faultAnalyze( String yearMonth,String type,String name,String dataType) {
        LinkedHashMap<String, Object> map = performanceAnalysisMapper.faultAnalyze(yearMonth,type,name,dataType);
        LinkedHashMap<String, Object> mapCnt = performanceAnalysisMapper.faultAnalyze(yearMonth,type,name,"2");
        //查询MTBF

        Map<String, Object>  result =  new HashMap<>();

        ArrayList<Object> resultList = new ArrayList<>();
        ArrayList<Object> resultListCnt = new ArrayList<>();
        {

            //数值集合
            ArrayList<Double> yList = new ArrayList(map.values());
            ArrayList<Object> xList = new ArrayList(map.keySet());
            xList.add("故障时长");
            //求和得总数
            Double sum = yList.stream().reduce(Double::sum).orElse(0.00);
            yList.add(sum);
            for (int i=0;i<yList.size();i++){
                if (yList.get(i)!=null){
                    Map<String, Object> xMap = new HashMap<>();
                    DecimalFormat df = new DecimalFormat("0.00");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    xMap.put("name",xList.get(i).toString());
                    xMap.put("value",df.format(yList.get(i)/sum*100));
                    resultList.add(xMap);
                }
            }
        }

        {

            //数值集合
            ArrayList<Double> yList = new ArrayList(mapCnt.values());
            ArrayList<Object> xList = new ArrayList(mapCnt.keySet());
            //求和得总数
            xList.add("故障次数");
            Double sum = yList.stream().reduce(Double::sum).orElse(0.00);
            yList.add(sum);
            for (int i=0;i<yList.size();i++){
                if (yList.get(i)!=null){
                    Map<String, Object> xMap = new HashMap<>();
                    DecimalFormat df = new DecimalFormat("0.00");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    xMap.put("name",xList.get(i).toString());
                    xMap.put("value",df.format(yList.get(i)/sum*100));
                    resultListCnt.add(xMap);
                }
            }
        }

        result.put("MTBF",500);
        result.put("时长",resultList);
        result.put("次数",resultListCnt);
        return Result.ok(result);
    }

    /**
     * 场站故障次数排名
     * @param yearMonth
     *  @param type 1 场站故障次数排名 2：场站故障时长排名 3：场站MTBF 健康状态排名
     * @return
     */
    @RequestMapping("faultTop10")
    @ResponseBody
    public Result faultTop10( String yearMonth,String type,String sortType) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.faultTop10(yearMonth,type,sortType);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("name",vo.getYData());
            map.put("value",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 地区逆变器故障多维分析-下拉列表
     *  @param type 1 断面 2：类型 3：集团 4:厂家
     * @return
     */
    @RequestMapping("faultAnalyzeDownList")
    @ResponseBody
    public Result faultAnalyzeDownList(String type) {
        List<String> useOfHoursVos = performanceAnalysisMapper.faultAnalyzeDownList(type);
        return Result.ok(useOfHoursVos);
    }
    public  Map<String, Object> selTrend(List<UseOfHoursVo> useOfHoursVos ) {
//        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.seluesOfHoursTrendDm(Year);

        Map<String, Object> map = new HashMap<>();
        List<String> yData1List=useOfHoursVos.stream().map(UseOfHoursVo::getYData1).distinct().collect(Collectors.toList());
        List<String> xList = useOfHoursVos.stream().map(UseOfHoursVo::getXData).distinct().collect(Collectors.toList());

        //                Collectors.collectingAndThen(Collectors.toList(),value->value.get(0))));
        Map<String, List<String>> mapd = new HashMap<>();
        //根据名称分组，并获取ydata数据
        for (UseOfHoursVo useOfHoursVo : useOfHoursVos) {
            mapd.computeIfAbsent(useOfHoursVo.getYData1(), k -> new ArrayList<>()).add(useOfHoursVo.getYData());
        }
        map.put("xList",xList);
        for (String  s:yData1List
        ) {
            map.put(s,mapd.get(s));
        }
        return map;
    }
}
