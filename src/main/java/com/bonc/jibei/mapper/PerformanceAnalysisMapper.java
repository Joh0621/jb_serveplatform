package com.bonc.jibei.mapper;

import com.bonc.jibei.vo.NumericalStatisticsVo;
import com.bonc.jibei.vo.UseOfHoursVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PerformanceAnalysisMapper {
    List<UseOfHoursVo> seluesOfHoursTrend(@Param("year") String year);

    List<UseOfHoursVo> selPrTrend(@Param("year") String year);


    List<UseOfHoursVo> selAccuracyRateTrend(@Param("year") String year);
}
