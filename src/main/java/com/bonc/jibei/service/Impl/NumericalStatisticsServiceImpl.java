package com.bonc.jibei.service.Impl;

import com.bonc.jibei.mapper.NumericalStatisticsMapper;
import com.bonc.jibei.service.NumericalStatisticsService;
import com.bonc.jibei.vo.NumericalStatisticsVo;
import com.bonc.jibei.vo.RadiationDoseDistributedVo;
import com.bonc.jibei.vo.SunHoursTrendVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NumericalStatisticsServiceImpl implements NumericalStatisticsService {
    @Resource
    private NumericalStatisticsMapper numericalStatisticsMapper;
    @Override
    public Map<String, Object> monitoringAnalysis(String year,String flag,String flag1) {
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
        List<NumericalStatisticsVo> numericalStatisticsVos = numericalStatisticsMapper.selMonitoringAnalysis(year,flag,flag1);
        for (NumericalStatisticsVo vo :numericalStatisticsVos){
            xList.add(vo.getDataTime());
        if ("1".equals(flag1)) {
            spList.add(vo.getRadiationDose());
            fzdList.add(vo.getPeakIrradiance());
            tqspList.add(vo.getRadiationDoseTq());
            tqfzdList.add(vo.getPeakIrradianceTq());
        }
            if ("2".equals(flag1)) {
                tqAvgTempList.add(vo.getAvgTemp());
                avgTempList.add(vo.getAvgTemp());
                maxTempList.add(vo.getMaxTempTq());
                minTempList.add(vo.getMinTempTq());
            }
            if ("3".equals(flag1)) {
                tqAvgWindList.add(vo.getAvgWindSpeed());
                avgWindList.add(vo.getAvgWindSpeed());
                maxWindList.add(vo.getMaxWindSpeedTq());
                minWindList.add(vo.getMinWindSpeedTq());
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
    public Map<String, Object> radiationDoseDistributed(String year, String flag, String flag1) {
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> dayList = new ArrayList<>();
        ArrayList<Object> rateList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<RadiationDoseDistributedVo> radiationDoseDistributedVos = numericalStatisticsMapper.selRadiationDoseDistributed( year, flag,flag1);
       //集合的cnt和
        double sum = radiationDoseDistributedVos.stream().mapToDouble(RadiationDoseDistributedVo::getCnt).sum();

//        for (RadiationDoseDistributedVo vo :radiationDoseDistributedVos) {
//            xList.add(vo.getValue());
//            dayList.add(vo.getCnt());
//            rateList.add(vo.getRate());
//        }
        double rate=100;
        for (int i = 0; i <radiationDoseDistributedVos.size() ; i++) {
            xList.add(radiationDoseDistributedVos.get(i).getValue());
            dayList.add(radiationDoseDistributedVos.get(i).getCnt());
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.UP);
            if (i==0){
                 rate=100;
                rateList.add(rate);
            }else {
                double v = radiationDoseDistributedVos.get(i-1).getCnt() / sum*100;
                 rate= Double.parseDouble(df.format(rate-v));
                rateList.add(rate);
            }

        }
        map.put("xList",xList);
        map.put("dayList",dayList);
        map.put("rateList",rateList);
        return map;
    }

    @Override
    public Map<String, Object> selSunHoursTrend(String year) {
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> over3List = new ArrayList<>();
        ArrayList<Object> over6List = new ArrayList<>();
        ArrayList<Object> over3TqList = new ArrayList<>();
        ArrayList<Object> over6TqList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<SunHoursTrendVo> radiationDoseDistributedVos = numericalStatisticsMapper.selSunHoursTrend(year);
        for (SunHoursTrendVo vo :radiationDoseDistributedVos) {
            xList.add(vo.getAName());
            over3List.add(vo.getOver3());
            over6List.add(vo.getOver6());
            over3TqList.add(vo.getOver3Tq());
            over6TqList.add(vo.getOver6Tq());
        }
        map.put("xList",xList);
        map.put("over3List",over3List);
        map.put("over6List",over6List);
        map.put("over3TqList",over3TqList);
        map.put("over6TqList",over6TqList);
        return map;
    }

}
