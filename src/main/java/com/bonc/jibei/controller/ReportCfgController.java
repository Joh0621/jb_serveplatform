package com.bonc.jibei.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.entity.ReportModel;
import com.bonc.jibei.mapper.ReportInterfaceMapper;
import com.bonc.jibei.mapper.ReportModelMapper;
import com.bonc.jibei.service.FileService;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/17 16:24
 * @Description: TODO
 */
@Api(tags = "报告配置接口")
@RestController
public class ReportCfgController {
    @Resource
    ReportModelMapper reportModelMapper;

    @Resource
    ReportInterfaceMapper reportInterfaceMapper;

    @Resource
    FileService ftpFileService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportModel.class),
    })
    @ApiOperation(value = "增加报告模板")
    @PostMapping("/model/add")
    public Result addBaseInfo(ReportModel reportModel) {
        String pathFile= null;
        try {
            pathFile = ftpFileService.upload(reportModel.getModelFile().getOriginalFilename(),reportModel.getModelFile().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reportModel.setModelFileUrl(pathFile);
        return Result.of(reportModelMapper.insert(reportModel));
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @ApiOperation(value = "增加接口")
    @PostMapping("/inter/add")
    public Result addInter(ReportInterface reportInterface) {
        return Result.of(reportInterfaceMapper.insert(reportInterface));
    }

    @ApiOperation(value = "删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口ID", required = true),
    })
    @PostMapping("/inter/del")
    public Result delInter(Integer id) {
        return Result.of(reportInterfaceMapper.deleteById(id));
    }
    @ApiOperation(value = "接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "name", value = "名称", required = false),
            @ApiImplicitParam(name = "type", value = "接口类型", required = false),

    })
    @PostMapping("/inter/select")
    public Result selectInter(@ApiIgnore Page<ReportInterface> page, String name, String type) {
        Page<ReportInterface> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ReportInterface>  list=reportInterfaceMapper.selectReportInterList(jpage,name,type);
        jpage.setRecords(list);
        jpage.setTotal(reportInterfaceMapper.selectCount(name,type));
        return Result.of(jpage);
    }
}
