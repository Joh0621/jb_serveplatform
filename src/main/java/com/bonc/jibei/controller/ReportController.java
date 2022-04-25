package com.bonc.jibei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.api.ResultCode;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.entity.ReportAuthLog;
import com.bonc.jibei.mapper.ReportMngMapper;

import com.bonc.jibei.mapper.ReportAuthLogMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.service.ReportAuthLogService;

import com.bonc.jibei.util.FileDownloadUtil;
import com.bonc.jibei.vo.ReportMngList;
import com.bonc.jibei.vo.ReportMngPatchPub;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 14:00s
 * @Description: TODO
 */
@Api(tags = "报告管理接口")
@RestController
public class ReportController {

    @Resource
    private ReportMngService reportMngService;

    @Resource
    private ReportMngMapper reportMngMapper;

    @Resource
    private ReportAuthLogMapper reportAuthLogMapper;

    @Resource
    private ReportAuthLogService reportAuthLogService;

    @Resource
    private FileDownloadUtil fileDownloadUtil;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "stationName", value = "场站名称", required = false),
            @ApiImplicitParam(name = "year", value = "年份", required = false),
            @ApiImplicitParam(name = "quarter", value = "季度", required = false),
            @ApiImplicitParam(name = "stationType", value = "场站类型", required = false),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportMngList.class),
    })
    @ApiOperation(value = "报告管理列表")
    @GetMapping("/report/list")
    public Result ReportMngList(@ApiIgnore Page<ReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus) {
        Page<ReportMngList> jpage = new Page<>(page.getCurrent(), page.getSize());
        long start = (page.getCurrent() - 1) * page.getSize();
        long size = page.getSize();
        jpage.setSearchCount(false);
        List<ReportMngList> list = reportMngService.reportMngList(jpage, stationName, year, quarter, stationType, reportStatus, start, size);
        jpage.setRecords(list);
//        Integer cnt = reportMngMapper.selectCount(stationName, year, quarter, stationType, reportStatus);
        jpage.setTotal(reportMngMapper.selectCount(stationName, year, quarter, stationType, reportStatus));
        return Result.of(jpage);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报告id", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态,1=发布.0=没发布", required = true),
            @ApiImplicitParam(name = "memo", value = "审批意见", required = false),
    })
    @ApiOperation(value = "报告管理复核发布")
    @GetMapping("/report/publish")
    public Result updateReportStatus(Integer id, Integer reportStatus, String memo) {

        QueryWrapper<ReportMng> qw = new QueryWrapper<>();
        qw.eq("id", id);

        ReportMng reportMng = reportMngMapper.selectById(id);
        if (reportMng == null) {
            return Result.error(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
        }
        reportMng.setReportStatus(reportStatus);
        reportMngMapper.updateById(reportMng);
        reportAuthLogService.insertReportAuth(id, "张章", "复核发布", memo);
        return Result.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报告id", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态,1=发布.0=没发布", required = true),
            @ApiImplicitParam(name = "memo", value = "审批意见", required = false),
    })
    @ApiOperation(value = "报告管理取消发布")
    @GetMapping("/report/pubcancell")
    public Result cancellReportStatus(Integer id, Integer reportStatus, String memo) {
        QueryWrapper<ReportMng> qw = new QueryWrapper<>();
        qw.eq("id", id);
        ReportMng ReportMng = reportMngMapper.selectById(id);
        if (ReportMng == null) {
            return Result.error(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
        }
        ReportMng.setReportStatus(reportStatus);
//        jbReportMng.setReportStatus(0);
        reportMngMapper.updateById(ReportMng);

        reportAuthLogService.insertReportAuth(id, "张章", "取消发布", memo);
        return Result.ok();
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsList", value = "报告id", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态,1=发布.0=没发布", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportMngList.class),
    })
    @ApiOperation(value = "批量复核")
    @PostMapping("/report/patchpublish")
    @ResponseBody
    public Result patchupdateReportStatus(@RequestBody(required = false) ReportMngPatchPub params) {
        List<String> msglist = null;
        Integer[] idsList = params.getIdsList();
        for (Integer id : idsList) {
            if (id == null || id <= 0) {
                continue;
            }
//            // 记录已复核发布的场站
//            if (obj.getReportStatus() == 1) {
//                msglist.add(obj.getReportYear() + "年第" + obj.getReportQuarter() + "季度,场站类型是" + obj.getStationTypeName() + "的" + obj.getStationName());
//                continue;
//            }
//            QueryWrapper<ReportMng> mngQw = new QueryWrapper<>();
//            mngQw.eq("id", id);
            // 更新报告状态
            ReportMng mng = reportMngMapper.selectById(id);
            mng.setReportStatus(1);
            reportMngMapper.updateById(mng);
        }
        return Result.of(msglist);
    }

    @ApiOperation(value = "报告管理审批记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报告id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportAuthLog.class),
    })
    @GetMapping("/report/loglist")
    public Result jbReportMngLogList(Integer id) {
        QueryWrapper<ReportAuthLog> qw = new QueryWrapper<>();
        qw.eq("report_id", id);
        List<ReportAuthLog> list = reportAuthLogMapper.selectList(qw);
        return Result.of(list);
    }

    @ApiOperation(value = "报告管理报告下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsList", value = "报告id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportAuthLog.class),
    })
    @GetMapping("/report/reportdownload")
    @ResponseBody
    @CrossOrigin
//    public Result reportDownload(@RequestBody(required = false) reportMngPatchPub params, HttpServletResponse response) {
    public Result reportDownload(@RequestParam(required = false) Integer[] idsList, HttpServletResponse response) {
        // 获取所有url
        List<Integer> idList = Arrays.asList(idsList);
        List<String> fileSrcPaths = reportMngService.urlList(idList);
        // 生成压缩包下载
        response.addHeader("Access-Allow-Control-Origin","*");
        fileDownloadUtil.downloadZipFiles(response, fileSrcPaths);
//        QueryWrapper<ReportAuthLog> qw = new QueryWrapper<>();
//        qw.eq("report_id", id);
//        List<ReportAuthLog> list = reportAuthLogMapper.selectList(qw);
        return Result.of("");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsList", value = "报告id", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态,1=发布.0=没发布 2=重新生成", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportMngList.class),
    })
    @ApiOperation(value = "重新生成")
    @PostMapping("/report/patchcreate")
    public Result patchUpdateStatus(@RequestParam(required = false) Integer[] idsList) {
        List<String> msglist = null;
        for (Integer id : idsList) {
            if (id == null || id <= 0) {
                continue;
            }
            // 更新报告状态
            ReportMng mng = reportMngMapper.selectById(id);
            mng.setReportStatus(2);
            reportMngMapper.updateById(mng);
        }
        return Result.of(msglist);
    }
}
