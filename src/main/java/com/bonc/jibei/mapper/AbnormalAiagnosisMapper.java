package com.bonc.jibei.mapper;

import com.bonc.jibei.entity.powerInverter;
import com.bonc.jibei.entity.powerUnitEvaluation;
import com.bonc.jibei.vo.UseOfHoursVo;
import com.bonc.jibei.vo.powerInverterStatusVo;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface AbnormalAiagnosisMapper {

    List<powerUnitEvaluation> powerUnitEvaluation(@Param("yearMonth") String yearMonth,
                                                  @Param("stationId") String stationId);

    List<UseOfHoursVo> powerUnitPower(@Param("yearMonth") String yearMonth,
                                                  @Param("stationId") String stationId,
                                      @Param("powerUnit") List<String> powerUnit
                                      );
  List<powerInverter>  powerInverterErrorPosition(@Param("yearMonth") String yearMonth,
                                          @Param("stationId") String stationId);

    powerInverterStatusVo  powerInverterErrorStatistics(@Param("yearMonth") String yearMonth,
                                                              @Param("stationId") String stationId);
    List<UseOfHoursVo> powerInverterconversionEfficiency(@Param("yearMonth") String yearMonth,
                                      @Param("stationId") String stationId,
                                      @Param("inverter") String inverter
    );
    List<UseOfHoursVo> powerInverterPower(@Param("yearMonth") String yearMonth,
                                          @Param("stationId") String stationId

    );

}
