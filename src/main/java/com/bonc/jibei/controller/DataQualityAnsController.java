package com.bonc.jibei.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.config.influx.InfluxDBClient;
import com.bonc.jibei.entity.*;
import com.bonc.jibei.mapper.DataQualityErrorMapper;
import com.bonc.jibei.service.DataQualityErrorService;
import com.bonc.jibei.service.QualifiedService;
import com.bonc.jibei.vo.DataQualityErrorVo;
import com.bonc.jibei.vo.ModelInterParamMapVo;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequestMapping("DataQualityAns")
@RestController
public class DataQualityAnsController {

    @Resource
    private DataQualityErrorService dataQualityErrorService;

    @Resource
    private DataQualityErrorMapper dataQualityErrorMapper;
    /**
     * @catalog  数据质量合格率统计
     * @type 1:风电 2：光伏
     * */
    @GetMapping("passRateStatistics")
    @ResponseBody
    public Result passRateStatistics(String startTime, String endTime, String type,String stationId) {
        PassRateStatistics result = dataQualityErrorService.passRateStatistics( startTime, endTime, type,stationId);
        return Result.ok(result);
    }

    /**
     * @catalog  排名后十场站
     * @type 1:风电 2：光伏
     * */
    @GetMapping("passRateDown10")
    @ResponseBody
    public Result passRateDown10(String startTime, String endTime, String type) {
        List<Station> result = dataQualityErrorMapper.passRateDown10(startTime, endTime, type);
        return Result.ok(result);
    }
    /**
     * @catalog  接入场站状态
     * @type 1:风电 2：光伏
     * */
    @GetMapping("selStationStatus")
    @ResponseBody
    public Result selStationStatus(String startTime, String endTime, String type) {
        List<Station> result = dataQualityErrorMapper.selStationStatus(startTime, endTime, type);
        return Result.ok(result);
    }
    /**
     * @catalog  接入场站数
     * @type 1:风电 2：光伏
     * */
    @GetMapping("selStationCnt")
    @ResponseBody
    public Result selStationCnt(String startTime, String endTime, String type) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> map = dataQualityErrorMapper.selStationCnt(startTime, endTime, type);
        //key list 为xData
        List<String> xList = map.keySet().stream()
                .collect(Collectors.toList());

        List<Object> yList = map.values().stream()
                .collect(Collectors.toList());
        long sum1 = map.values().stream().collect(Collectors.summarizingInt(x -> Integer.parseInt(x.toString()))).getSum();

        resultMap.put("count",sum1);
        resultMap.put("xData",xList);
        resultMap.put("yData",yList);

        return Result.ok(resultMap);
    }


    /**
     * 数据质量合格率趋势/场站数据质量合格率
     * 根据查询条件区分
     */
    @RequestMapping("passRateTrend")
    @ResponseBody
    public Result passRateTrend(String startTime, String endTime, String type,String stationId,String dataFlag) {
        List<Map<String,Object>> result=dataQualityErrorService.selPassRateTrend( startTime, endTime, type,stationId,dataFlag);
        return Result.ok(result);
    }

    /**
     *  异常记录
     */
    @RequestMapping("errorRecord")
    @ResponseBody
    public Result errorRecord(@ApiIgnore Page<InterParams> page,String dataSource, String errorType, String stationId, String DeviceId,String startTime, String endTime ) {
        Page<DataQualityError> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<DataQualityError> result=   dataQualityErrorService.selErrorRecord(jpage,dataSource,errorType,stationId,DeviceId, startTime,  endTime);
        jpage.setRecords(result);
        jpage.setTotal(dataQualityErrorMapper.selErrorRecordTotal(dataSource, errorType, stationId, DeviceId,startTime,  endTime).getCnt());
        jpage.setCountId(String.valueOf(dataQualityErrorMapper.selErrorRecordTotal(dataSource, errorType, stationId, DeviceId,startTime,  endTime).getNum()));
        return Result.of(jpage);
    }

    /**
     *数据质量问题分布
     */
    @RequestMapping("errorDistributed")
    @ResponseBody
    public Result errorDistributed(String startTime, String endTime,String stationId , String type) {
        Map<String,Object> result=   dataQualityErrorService.selErrorDistributed(startTime,endTime,type,stationId);
        return  Result.ok(result);
    }

    /**
     * 异常数据统计

     * @param id
     * @return
     */
    @RequestMapping("errorDataStatistics")
    @ResponseBody
    public Result errorDataStatistics(String id ) {
        List<Map<String, Object>> result=   dataQualityErrorService.errorDataStatistics(id);
        return  Result.ok(result);
    }

    /**
     *
     * @param type 1 风电场站 2:光伏场站
     *  @param aname 1 风电场站 2:光伏场站
     * @return
     */
    @RequestMapping("selStation")
    @ResponseBody
    public Result selStation(String type,String aname) {
        List<Station> result=   dataQualityErrorMapper.selStation(type,aname);
        for (Station rs:result
        ) {
            if (rs.getLit()!=null){
               rs.setLat(rs.getLit().substring(13).split(",")[1].substring(11).replace("}",""));
                rs.setLit(rs.getLit().substring(13).split(",")[0]);
            }
        }
        return  Result.ok(result);
    }

    /**
     *
     * @param dataSource 1 风电场站 2:光伏场站
     *  @param errorType 1 风电场站 2:光伏场站
     * @return
     */
    @RequestMapping("selDevice")
    @ResponseBody
    public Result selDevice(String dataSource,String errorType,String stationId,String type) {
        List<String> result=   dataQualityErrorMapper.selDevice(dataSource,errorType,stationId,type);
        return  Result.ok(result);
    }
//    @RequestMapping("errorDataStatisticsDetail")
//    @ResponseBody
//    public Result errorDataStatisticsDetail(String startTime, String endTime,String stationId , String dataSource,String errorType) {
//
//
//    }


    @RequestMapping("outPassRateTrend")
    @ResponseBody
    public void outPassRateTrend(HttpServletResponse response, String startTime, String endTime, String type, String stationId,String dataFlag) throws IOException {
        List<Qualified> result = dataQualityErrorMapper.SelPassRateTrend(startTime, endTime, type,stationId,dataFlag);

        // 预设Excel表格
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 设置sheet名
        HSSFSheet sheet = workbook.createSheet("1");
        HSSFRow qualityRow1 = sheet.createRow(0);

        HSSFCellStyle styleHeaderCell = workbook.createCellStyle();
        styleHeaderCell.setAlignment(HorizontalAlignment.CENTER);

        qualityRow1.createCell(0).setCellValue("日期");
        qualityRow1.createCell(1).setCellValue("总合格率");
        qualityRow1.createCell(2).setCellValue("海量平台");
        qualityRow1.createCell(3).setCellValue("单机系统");
        qualityRow1.createCell(4).setCellValue("场站实时数据");
        qualityRow1.createCell(5).setCellValue("功率预测数据");
        String fileName = URLEncoder.encode("风电场数据质量.xls", "UTF-8");

        for (int i = 0; i < result.size(); i++) {
            HSSFRow qualityRow = sheet.createRow(1 + i);
            System.out.println(result.get(i).getDateTime().toString());
            qualityRow.createCell(0).setCellValue(result.get(i).getDateTime().toString());
            qualityRow.createCell(1).setCellValue(result.get(i).getPassRate());
            qualityRow.createCell(2).setCellValue(result.get(i).getHlpt());
            qualityRow.createCell(3).setCellValue(result.get(i).getDjxt());
            qualityRow.createCell(4).setCellValue(result.get(i).getCzsssj());
            qualityRow.createCell(5).setCellValue(result.get(i).getGlycsj());
        }

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    /**
     *数据质量问题分布导出
     */
    @RequestMapping("outErrorDistributed")
    @ResponseBody
    public void outErrorDistributed(HttpServletResponse response, String startTime, String endTime,String stationId , String type) throws IOException {
        List<DataQualityErrorVo> dataQualityErrorForData = dataQualityErrorMapper.selErrorDistributedForData(startTime, endTime, type, stationId);
        List<DataQualityErrorVo> dataQualityErrorForError = dataQualityErrorMapper.selErrorDistributedForError(startTime, endTime, type, stationId);
        // 预设Excel表格
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 设置sheet名
        HSSFSheet sheet = workbook.createSheet("1");
        HSSFRow qualityRow1 = sheet.createRow(0);
        Cell cell = qualityRow1.createCell(0);
        HSSFCellStyle styleHeaderCell = workbook.createCellStyle();
        styleHeaderCell.setAlignment(HorizontalAlignment.CENTER);
        qualityRow1.createCell(0).setCellValue("缺数");
        qualityRow1.createCell(1).setCellValue("死数");
        qualityRow1.createCell(2).setCellValue("错数");


        String fileName = URLEncoder.encode("风电场数据质量.xls", "UTF-8");

        for (int i = 0; i < dataQualityErrorForData.size(); i++) {
            HSSFRow qualityRow = sheet.createRow(1 + i);
            System.out.println(dataQualityErrorForData.get(i).getHlpt());
            qualityRow.createCell(0).setCellValue(dataQualityErrorForData.get(i).getMissData());
            qualityRow.createCell(1).setCellValue(dataQualityErrorForData.get(i).getDeadData());
            qualityRow.createCell(2).setCellValue(dataQualityErrorForData.get(i).getErrorData());
        }

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

}
