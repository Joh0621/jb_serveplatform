package com.bonc.jibei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.api.ResultCode;
import com.bonc.jibei.entity.JbReportMng;
import com.bonc.jibei.entity.ReportAuthLog;
import com.bonc.jibei.mapper.JbReportMngMapper;

import com.bonc.jibei.mapper.ReportAuthLogMapper;
import com.bonc.jibei.service.JbReportMngService;

import com.bonc.jibei.service.ReportAuthLogService;
import com.bonc.jibei.vo.JbReportMngList;
import com.bonc.jibei.vo.JbReportMngPatchPub;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
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
    private JbReportMngService jbReportMngService;
    @Resource
    private JbReportMngMapper jbReportMngMapper;

    @Resource
    private ReportAuthLogMapper reportAuthLogMapper;

    @Resource
    private ReportAuthLogService reportAuthLogService;

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
            @ApiResponse(code = 200, message = "OK", response = JbReportMngList.class),
    })
    @ApiOperation(value = "报告管理列表")
    @GetMapping("/report/list")
    public Result jbReportMngList(@ApiIgnore Page<JbReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus) {
        Page<JbReportMngList> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<JbReportMngList> list = jbReportMngService.jbReportMngList(jpage, stationName, year, quarter, stationType, reportStatus);
        jpage.setRecords(list);
        Integer cnt = jbReportMngMapper.selectCount(stationName, year, quarter, stationType, reportStatus);
        jpage.setTotal(jbReportMngMapper.selectCount(stationName, year, quarter, stationType, reportStatus));
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

        QueryWrapper<JbReportMng> qw = new QueryWrapper<>();
        qw.eq("id", id);

        JbReportMng jbReportMng = jbReportMngMapper.selectById(id);
        if (jbReportMng == null) {
            return Result.error(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
        }
        jbReportMng.setReportStatus(reportStatus);
//        jbReportMng.setReportStatus(1);
        jbReportMngMapper.updateById(jbReportMng);
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
        QueryWrapper<JbReportMng> qw = new QueryWrapper<>();
        qw.eq("id", id);
        JbReportMng jbReportMng = jbReportMngMapper.selectById(id);
        if (jbReportMng == null) {
            return Result.error(ResultCode.NOT_FOUND.getCode(), ResultCode.NOT_FOUND.getMessage());
        }
        jbReportMng.setReportStatus(reportStatus);
//        jbReportMng.setReportStatus(0);
        jbReportMngMapper.updateById(jbReportMng);

        reportAuthLogService.insertReportAuth(id, "张章", "取消发布", memo);
        return Result.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsList", value = "报告id", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态,1=发布.0=没发布", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = JbReportMngList.class),
    })
    @ApiOperation(value = "批量复核")
    @PostMapping("/report/patchpublish")
    @ResponseBody
    public Result patchupdateReportStatus(@RequestBody(required = false) JbReportMngPatchPub params) {
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
//            QueryWrapper<JbReportMng> mngQw = new QueryWrapper<>();
//            mngQw.eq("id", id);
            // 更新报告状态
            JbReportMng mng = jbReportMngMapper.selectById(id);
            mng.setReportStatus(1);
            jbReportMngMapper.updateById(mng);
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
}
