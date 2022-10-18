package com.bonc.jibei.mapper;

import com.bonc.jibei.vo.NumericalStatisticsVo;
import com.bonc.jibei.vo.SortVo;
import com.bonc.jibei.vo.UseOfHoursVo;
import org.apache.ibatis.annotations.Param;

import java.util.*;

public interface PerformanceAnalysisMapper {
    List<UseOfHoursVo> seluesOfHoursTrend(@Param("year") String year);


    UseOfHoursVo  powerGenerationIndex(@Param("year") String year,
                         @Param("type") String type);

    SortVo powerGenerationIndexCz(@Param("startTime") String startTime,
                                  @Param("endTime") String endTime,
                                  @Param("type") String type,
                                  @Param("stationId") String stationId);

    SortVo  powerGenerationIndexCzSortDm(@Param("startTime") String startTime,
                                               @Param("endTime") String endTime,
                                         @Param("type") String type,
                                         @Param("stationId") String stationId);

    List<UseOfHoursVo> seluesOfHoursTrendDq(@Param("year") String year);

    List<UseOfHoursVo> powerGenerationOverview(@Param("startTime") String startTime,
                                  @Param("endTime") String endTime,
                                  @Param("stationId") String stationId);

    List<UseOfHoursVo> uesOfHoursTrendCz(@Param("startTime") String startTime,
                                         @Param("endTime") String endTime,
                                         @Param("stationId") String stationId,
                                         @Param("table") String table,
                                         @Param("param") String param,
                                         @Param("time") String time);

    List<UseOfHoursVo> seluesOfHoursTrendDm(@Param("year") String year);

    List<UseOfHoursVo> selUesOfHoursStationTop10(@Param("year") String year,
                                                 @Param("sortType") String sortType );

    List<UseOfHoursVo> selUesOfHoursCompanyTop10(@Param("year") String year,
                                                 @Param("sortType") String sortType );

    List<UseOfHoursVo> selPrTrend(@Param("year") String year);

    List<UseOfHoursVo> selPrTrendDq(@Param("year") String year);


    List<UseOfHoursVo> selAvgPrAnalysis(@Param("year") String year,
                                                 @Param("type") String type );
    Map<String, Object> selPrTrendDqValue(@Param("year") String year);

    List<String> selPrTrendDqName(@Param("year") String year);

    List<UseOfHoursVo> prStationTop10(@Param("year") String year,
                                      @Param("sortType") String sortType );

    List<UseOfHoursVo> prCompanyTop10(@Param("year") String year,
                                      @Param("sortType") String sortType );

    List<UseOfHoursVo> selAccuracyRateTrend(@Param("year") String year);

    List<UseOfHoursVo> AccuracyRateCompany(@Param("startTime") String startTime,
                                           @Param("endTime") String endTime);

    List<UseOfHoursVo> AccuracyRateTop10(@Param("year") String year,
                                           @Param("type") String type,
                                         @Param("sortType") String sortType);




    LinkedHashMap<String, Object> faultInfoType(@Param("year") String year);
    LinkedHashMap<String, Double> runningStatus(@Param("yearMonth") String yearMonth);

    LinkedHashMap<String, Object> faultAnalyze(@Param("yearMonth") String yearMonth,
                                               @Param("type") String type,
                                               @Param("name") String name,
                                               @Param("dataType") String dataType);



    String faultAnalyzeMTBF(@Param("yearMonth") String yearMonth,
                                               @Param("type") String type,
                                               @Param("name") String name);
    List<UseOfHoursVo> faultTop10(@Param("yearMonth") String yearMonth,
                                  @Param("type") String type,
                                  @Param("sortType") String sortType);

    List<String> faultAnalyzeDownList(@Param("type") String type);

}
