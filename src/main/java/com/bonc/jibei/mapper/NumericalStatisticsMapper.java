package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.NumericalStatistics;
import com.bonc.jibei.entity.PassRateStatistics;
import com.bonc.jibei.entity.Qualified;
import com.bonc.jibei.vo.DataQualityErrorVo;
import com.bonc.jibei.vo.NumericalStatisticsVo;
import com.bonc.jibei.vo.RadiationDoseDistributedVo;
import com.bonc.jibei.vo.SunHoursTrendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description NumericalStatistics
 * @author wangtao
 * @date 2022-08-08
 */
@Mapper
@Repository
public interface NumericalStatisticsMapper {

    List<NumericalStatisticsVo>   selMonitoringAnalysis(@Param("year") String year,
                                                        @Param("flag") String flag ,
                                                        @Param("flag1") String flag1);

    List<NumericalStatisticsVo>   selMonitoringAnalysisDq(@Param("year") String year,
                                                        @Param("flag1") String flag1);

    List<RadiationDoseDistributedVo>   selRadiationDoseDistributed(
                                                                   @Param("year") String year,
                                                                   @Param("flag") String flag ,
                                                                   @Param("flag1") String flag1 );

    List<SunHoursTrendVo>   selSunHoursTrend(@Param("year") String year);

}
