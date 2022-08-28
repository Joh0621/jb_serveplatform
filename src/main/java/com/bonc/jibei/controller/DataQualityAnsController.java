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
import java.util.List;
import java.util.Map;


@RequestMapping("DataQualityAns")
@RestController
public class DataQualityAnsController {

    @Resource
    private DataQualityErrorService dataQualityErrorService;

    @Resource
    private DataQualityErrorMapper dataQualityErrorMapper;
    /**
     * @catalog  数据质量合格率统计
     *
     * */
    @GetMapping("passRateStatistics")
//    @ResponseBody
    public Result passRateStatistics(String startTime, String endTime, String type,String stationId) {
        PassRateStatistics result = dataQualityErrorService.passRateStatistics( startTime, endTime, type,stationId);
        return Result.ok(result);
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
    public Result errorRecord(@ApiIgnore Page<InterParams> page,String dataSource, String errorType, String stationId, String DeviceId ) {
        Page<DataQualityError> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<DataQualityError> result=   dataQualityErrorService.selErrorRecord(jpage,dataSource,errorType,stationId,DeviceId);
        jpage.setRecords(result);
        jpage.setTotal(dataQualityErrorMapper.selErrorRecordTotal(dataSource, errorType, stationId, DeviceId));
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
     * @param startTime
     * @param endTime
     * @param stationId
     * @param dataSource
     * @return
     */
    @RequestMapping("errorDataStatistics")
    @ResponseBody
    public Result errorDataStatistics(String startTime, String endTime,String stationId , String dataSource,String errorType,String code) {
        List<Map<String, Object>> result=   dataQualityErrorService.errorDataStatistics(startTime,endTime,stationId,dataSource,errorType,code);
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
