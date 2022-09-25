package com.bonc.jibei.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description data_quality_error
 * @author wangtao
 * @date 2022-08-08
 */
@Service
public interface NumericalStatisticsService {


    Map<String,Object>  monitoringAnalysis(String year,String flag,String flag1);




    Map<String,Object>  radiationDoseDistributed ( String year,String flag,String flag1);


    Map<String,Object>  selSunHoursTrend (String year);

}