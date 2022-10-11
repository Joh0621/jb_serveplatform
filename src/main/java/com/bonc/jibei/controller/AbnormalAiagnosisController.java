package com.bonc.jibei.controller;

import com.bonc.jibei.api.Result;
import com.bonc.jibei.mapper.AbnormalAiagnosisMapper;
import com.bonc.jibei.mapper.PerformanceAnalysisMapper;
import com.bonc.jibei.vo.UseOfHoursVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerUnitEvaluation(yearMonth,stationId);

        Map<String, Object> map = new HashMap<>();
        ArrayList<Object> xList = new ArrayList<>();
        ArrayList<Object> yList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){

            xList.add(vo.getXData());
            yList.add(vo.getYData());
        }
        map.put("xData",xList);
        map.put("yData",yList);
        return Result.ok(map);
    }
}
