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
    public Map<String, Object> monitoringAnalysis(String startTime, String endTime,String flag) {
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> spList = new ArrayList<>();
        ArrayList<Object> fzdList = new ArrayList<>();
        ArrayList<Object> tqspList = new ArrayList<>();
        ArrayList<Object> tqfzdList = new ArrayList<>();
        ArrayList<Object> dqList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<NumericalStatisticsVo> numericalStatisticsVos = numericalStatisticsMapper.selMonitoringAnalysis(startTime, endTime,flag);
        for (NumericalStatisticsVo vo :numericalStatisticsVos){
            spList.add(vo.getRadiationDose() );
            fzdList.add( vo.getPeakIrradiance() );
            xList.add( vo.getYearMonthDate() );
            tqspList.add( vo.getRadiationDose() );
            tqfzdList.add(vo.getPeakIrradiance() );
            dqList.add(vo.getAName() );
        }
        map.put("时间",xList);
        map.put("水平",spList);
        map.put("同期水平",tqspList);
        map.put("fzd",fzdList);
        map.put("tqfzd",tqfzdList);
        map.put("地区",dqList);
        return  map;
    }

    @Override
    public Map<String, Object> radiationDoseDistributed(String startTime, String endTime, String flag) {
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> dayList = new ArrayList<>();
        ArrayList<Object> rateList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<RadiationDoseDistributedVo> radiationDoseDistributedVos = numericalStatisticsMapper.selRadiationDoseDistributed(startTime, endTime, flag);
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
        Map<String, Object> map = new HashMap<>();
        List<SunHoursTrendVo> radiationDoseDistributedVos = numericalStatisticsMapper.selSunHoursTrend(startTime, endTime);
        for (SunHoursTrendVo vo :radiationDoseDistributedVos) {
            xList.add(vo.getAName());
            over3List.add(vo.getOver3());
            over6List.add(vo.getOver6());
        }
        map.put("xList",xList);
        map.put("over3List",over3List);
        map.put("over6List",over6List);
        return map;
    }

}
