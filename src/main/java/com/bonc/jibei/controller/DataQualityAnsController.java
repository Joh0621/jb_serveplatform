package com.bonc.jibei.controller;


import com.bonc.jibei.api.Result;
import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.PassRateStatistics;
import com.bonc.jibei.service.DataQualityErrorService;
import com.bonc.jibei.service.QualifiedService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RequestMapping("DataQualityAns")
@RestController
public class DataQualityAnsController {

    @Resource
    private DataQualityErrorService dataQualityErrorService;
    /**
     * @catalog  数据质量合格率统计
     *
     * */
    @GetMapping("PassRateStatistics")
//    @ResponseBody
    public Result passRateStatistics(String startTime, String endTime, String type) {
        PassRateStatistics result = dataQualityErrorService.passRateStatistics( startTime, endTime, type);
        return Result.ok(result);
    }

    /**
     * 数据质量合格率趋势
     */
    @RequestMapping("PassRateTrend")
    @ResponseBody
    public Result passRateTrend(String startTime, String endTime, String type) {
        List<Map<String,Object>> result=dataQualityErrorService.selPassRateTrend( startTime, endTime, type);
        return Result.ok(result);
    }

/**
 *  异常记录
 */
    @RequestMapping("ErrorRecord")
    @ResponseBody
    public Result ErrorRecord(String dataSource,String errorType, String stationId, String DeviceId ) {
        List<DataQualityError> result=   dataQualityErrorService.selErrorRecord(dataSource,errorType,stationId,DeviceId);
        return  Result.ok(result);
    }
}
