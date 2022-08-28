package com.bonc.jibei.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.PassRateStatistics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description data_quality_error
 * @author wangtao
 * @date 2022-08-08
 */
@Service
public interface NumericalStatisticsService {


    Map<String,Object>  monitoringAnalysis(String startTime, String endTime,String flag,String flag1);


    Map<String,Object>  radiationDoseDistributed (String startTime, String endTime,String flag,String flag1);


    Map<String,Object>  selSunHoursTrend (String startTime, String endTime);

}