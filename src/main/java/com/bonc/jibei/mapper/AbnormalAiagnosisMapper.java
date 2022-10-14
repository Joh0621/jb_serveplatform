package com.bonc.jibei.mapper;

import com.bonc.jibei.entity.powerComponentsString;
import com.bonc.jibei.entity.powerInverter;
import com.bonc.jibei.entity.powerInverterWarning;
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
    List<UseOfHoursVo> powerInverterFaultCnt(@Param("yearMonth") String yearMonth,
                                          @Param("stationId") String stationId

    );

    List<powerInverterWarning> powerInverterWarning(@Param("yearMonth") String yearMonth,
                                                    @Param("stationId") String stationId

    );

    List<powerInverterWarning> powerInverterWarningDetail(@Param("inverter") String inverter,
                                                    @Param("warningTime") String warningTime,
                                                          @Param("warningType") String warningType,
                                                          @Param("warningDesc") String warningDesc

    );

    List<powerComponentsString> powerComponentsStringStatistics(@Param("yearMonth") String yearMonth,
                                                                @Param("stationId") String stationId

    );
  List<UseOfHoursVo> powerComponentsStringTop5(@Param("yearMonth") String yearMonth,
                                                              @Param("stationId") String stationId

  );
  List<powerComponentsString> powerComponentsStringList(@Param("yearMonth") String yearMonth,
                                                              @Param("stationId") String stationId

  );


}
