package com.bonc.jibei.service.Impl;

import com.bonc.jibei.mapper.DataQualityErrorMapper;
import com.bonc.jibei.mapper.NumericalStatisticsMapper;
import com.bonc.jibei.service.NumericalStatisticsService;
import com.bonc.jibei.vo.NumericalStatisticsVo;
import com.bonc.jibei.vo.RadiationDoseDistributedVo;
import com.bonc.jibei.vo.SunHoursTrendVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class NumericalStatisticsServiceImpl implements NumericalStatisticsService {
    @Resource
    private NumericalStatisticsMapper numericalStatisticsMapper;
    @Override
    public Map<String, Object> monitoringAnalysis(String startTime, String endTime,String flag,String flag1) {
        ArrayList<Object> xList = new ArrayList<>();
        if (flag1==null||"".equals(flag1)) {
            flag="1";
        }
        ArrayList<Object> spList = new ArrayList<>();
        ArrayList<Object> fzdList = new ArrayList<>();
        ArrayList<Object> tqspList = new ArrayList<>();
        ArrayList<Object> tqfzdList = new ArrayList<>();

        //温度监测分析

        ArrayList<Object> tqAvgTempList = new ArrayList<>();
        ArrayList<Object> avgTempList = new ArrayList<>();
        ArrayList<Object> maxTempList = new ArrayList<>();
        ArrayList<Object> minTempList = new ArrayList<>();

        //地面风速监测分析

        ArrayList<Object> tqAvgWindList = new ArrayList<>();
        ArrayList<Object> avgWindList = new ArrayList<>();
        ArrayList<Object> maxWindList = new ArrayList<>();
        ArrayList<Object> minWindList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        System.out.println(flag1);
        List<NumericalStatisticsVo> numericalStatisticsVos = numericalStatisticsMapper.selMonitoringAnalysis(startTime, endTime,flag,flag1);
        for (NumericalStatisticsVo vo :numericalStatisticsVos){
            xList.add(vo.getYearMonthDate());
        if ("1".equals(flag1)) {
            spList.add(vo.getRadiationDose());
            fzdList.add(vo.getPeakIrradiance());
            tqspList.add(vo.getRadiationDose());
            tqfzdList.add(vo.getPeakIrradiance());
        }
            if ("2".equals(flag1)) {
                tqAvgTempList.add(vo.getAvgTemp());
                avgTempList.add(vo.getAvgTemp());
                maxTempList.add(vo.getMaxTemp());
                minTempList.add(vo.getMinTemp());
            }
            if ("3".equals(flag1)) {
                tqAvgWindList.add(vo.getAvgWindSpeed());
                avgWindList.add(vo.getAvgWindSpeed());
                maxWindList.add(vo.getMaxWindSpeed());
                minWindList.add(vo.getMinWindSpeed());
            }
        }
        map.put("时间",xList);
        if ("1".equals(flag1)) {
            map.put("水平总辐射量", spList);
            map.put("同期水平总辐射量", tqspList);
            map.put("峰值辐照度", fzdList);
            map.put("同期峰值辐照度", tqfzdList);
        }
        if ("2".equals(flag1)) {
            map.put("平均气温同期", tqAvgTempList);
            map.put("平均气温", avgTempList);
            map.put("最高气温", maxTempList);
            map.put("最低气温", minTempList);
        }
        if ("3".equals(flag1)) {
            map.put("平均风速同期", tqAvgWindList);
            map.put("平均风速", avgWindList);
            map.put("最高风速", maxWindList);
            map.put("最低风速", minWindList);
        }
        return  map;
    }

    @Override
    public Map<String, Object> radiationDoseDistributed(String startTime, String endTime, String flag,String flag1) {
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> dayList = new ArrayList<>();
        ArrayList<Object> rateList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        System.out.println(flag1);
        List<RadiationDoseDistributedVo> radiationDoseDistributedVos = numericalStatisticsMapper.selRadiationDoseDistributed(startTime, endTime, flag,flag1);
        for (RadiationDoseDistributedVo vo :radiationDoseDistributedVos) {
            xList.add(vo.getValue());
            dayList.add(vo.getCnt());
            rateList.add(vo.getRate());
        }
        map.put("xList",xList);
        map.put("dayList",dayList);
        map.put("rateList",rateList);
        return map;
    }

    @Override
    public Map<String, Object> selSunHoursTrend(String startTime, String endTime) {
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> over3List = new ArrayList<>();
        ArrayList<Object> over6List = new ArrayList<>();
        ArrayList<Object> over3TqList = new ArrayList<>();
        ArrayList<Object> over6TqList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<SunHoursTrendVo> radiationDoseDistributedVos = numericalStatisticsMapper.selSunHoursTrend(startTime, endTime);
        for (SunHoursTrendVo vo :radiationDoseDistributedVos) {
            xList.add(vo.getAName());
            over3List.add(vo.getOver3());
            over6List.add(vo.getOver6());
            over3TqList.add(vo.getOver3());
            over6TqList.add(vo.getOver6());
        }
        map.put("xList",xList);
        map.put("over3List",over3List);
        map.put("over6List",over6List);
        map.put("over3TqList",over3TqList);
        map.put("over6TqList",over6TqList);
        return map;
    }

}
