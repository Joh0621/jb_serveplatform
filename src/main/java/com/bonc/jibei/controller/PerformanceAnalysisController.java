package com.bonc.jibei.controller;


import com.bonc.jibei.api.Result;
import com.bonc.jibei.mapper.PerformanceAnalysisMapper;
import com.bonc.jibei.vo.UseOfHoursVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("PerformanceAnalysis")
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
     * 功率预测准确度趋势
     */
    @RequestMapping("prTrend")
    @ResponseBody
    public Result selAccuracyRateTrend( String Year) {
        List<UseOfHoursVo> useOfHoursVos = performanceAnalysisMapper.selAccuracyRateTrend(Year);

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
}
