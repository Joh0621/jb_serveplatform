package com.bonc.jibei.controller;

import com.bonc.jibei.api.Result;
import com.bonc.jibei.mapper.AbnormalAiagnosisMapper;
import com.bonc.jibei.vo.UseOfHoursVo;
import com.bonc.jibei.vo.powerInverterStatusVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("abnormalAiagnosis")
@RestController
//场站异常诊断
public class AbnormalAiagnosisController {
    @Resource
    private AbnormalAiagnosisMapper abnormalAiagnosisMapper;

    /**
     * 发电单元发电性能与稳定性整体评价
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerUnitEvaluation")
    @ResponseBody
    public Result powerUnitEvaluation(String yearMonth,String stationId) {
        return Result.ok(abnormalAiagnosisMapper.powerUnitEvaluation(yearMonth, stationId));
    }


    /**
     * 发电单元功率曲线
     * @param yearMonth
     * @param stationId
     * @param powerUnit  发电单元
     * @return
     */
    @RequestMapping("powerUnitPower")
    @ResponseBody
    public Result powerUnitPower(String yearMonth,String stationId,String powerUnit) {
        List<String> strings = Arrays.asList(powerUnit.split(","));
       List< Map<String, Object>> resultMap = new ArrayList<>();
        List<UseOfHoursVo> powerUnitPower = abnormalAiagnosisMapper.powerUnitPower(yearMonth, stationId, strings);
        Map<String, List<UseOfHoursVo>> map = powerUnitPower.stream().collect(
                Collectors.groupingBy(
                        model -> model.getYData1()
                ));
        Map<String, Object> bData = new HashMap<>();
        for (Map.Entry<String, List<UseOfHoursVo>> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            ArrayList<Object> xList = new ArrayList<>();
            ArrayList<Object> yList = new ArrayList<>();
            Map<String, Object> mapData = new HashMap<>();
            for ( UseOfHoursVo value : entry.getValue()) {
                    xList.add(value.getXData());
                    yList.add(value.getYData());
            }
            mapData.put("xData",xList);
            mapData.put("yData",yList);
            bData.put(entry.getKey(),mapData);
        }
        return Result.ok(bData);
    }


    /**
     * 逆变器发电异常监测
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerinverterError")
    @ResponseBody
    public Result powerinverterError(String yearMonth,String stationId) {
        return Result.ok(abnormalAiagnosisMapper.powerInverterErrorPosition(yearMonth, stationId));
    }

    /**
     * 逆变器发电异常监测
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerInverterErrorStatistics")
    @ResponseBody
    public Result powerInverterErrorStatistics(String yearMonth,String stationId) {
        powerInverterStatusVo vo = abnormalAiagnosisMapper.powerInverterErrorStatistics(yearMonth, stationId);
        return Result.ok(vo);
    }
    /**
     * 逆变器转换效率
     * @param yearMonth
     * @param inverter
     * @return
     */
    @RequestMapping("powerInverterconversionEfficiency")
    @ResponseBody
    public Result powerInverterconversionEfficiency(String yearMonth,String stationId,String inverter) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerInverterconversionEfficiency(yearMonth, stationId, inverter);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 逆变器发电量排名TOP10
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerInverterPower")
    @ResponseBody
    public Result powerInverterPower(String yearMonth,String stationId) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerInverterPower(yearMonth, stationId);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

}
