package com.bonc.jibei.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.EnumValue;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.api.ResultCode;
import com.bonc.jibei.entity.*;
import com.bonc.jibei.mapper.*;
import com.bonc.jibei.service.FileService;

import com.bonc.jibei.vo.IdlistVo;
import com.bonc.jibei.vo.KeyValueVO;
import com.bonc.jibei.vo.ModelInterfaceRelListVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
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
    InterParamsMapper interParamsMapper;

    @Resource
    ModelInterfaceRelMapper modelInterfaceRelMapper;

    @Resource
    ReportParamsMapMapper reportParamsMapMapper;

    @Resource
    FileService ftpFileService;


    @ApiOperation(value = "i增加接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @PostMapping("/inter/add")
    public Result addInter(ReportInterface reportInterface) {
        return Result.of(reportInterfaceMapper.insert(reportInterface));
    }

    @ApiOperation(value = "i删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口ID", required = true),
    })
    @PostMapping("/inter/del")
    public Result delInter(Integer id) {
        return Result.of(reportInterfaceMapper.deleteById(id));
    }

    @ApiOperation(value = "i接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "name", value = "名称", required = false),
            @ApiImplicitParam(name = "type", value = "接口类型", required = false),
            @ApiImplicitParam(name = "modelId", value = "模板Id", required = false),
    })
    @PostMapping("/inter/select")
    public Result selectInter(@ApiIgnore Page<ReportInterface> page, String name, String type,Integer modelId) {
        Page<ReportInterface> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ReportInterface>  list=reportInterfaceMapper.selectReportInterList(jpage,name,type,modelId);
        jpage.setRecords(list);
        jpage.setTotal(reportInterfaceMapper.selectCount(name,type,modelId));
        return Result.of(jpage);
    }

    @ApiOperation(value = "i根据接口编码取接口数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @GetMapping("/inter/interbycode")
    public Result selectInterByCode(String code) {
        if (code==null || "".equals(code)) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        return Result.of(EnumValue.getUserTypeName());
    }

    @ApiOperation(value = "i接口类型下拉框")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = KeyValueVO.class),
    })
    @GetMapping("/inter/intertype")
    public Result selectInterType() {
        return Result.of(EnumValue.getUserTypeName());
    }

    @ApiOperation(value = "p增加接口参数")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @PostMapping("/param/add")
    public Result addInterParam(InterParams interParams) {
        if (interParams==null) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        return Result.of(interParamsMapper.insert(interParams));
    }

    @ApiOperation(value = "p删除接口参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "参数ID", required = true),
    })
    @PostMapping("/param/del")
    public Result delInterParam(Integer id) {
        return Result.of(interParamsMapper.deleteById(id));
    }

    @ApiOperation(value = "p接口参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "interId", value = "接口Id", required = true),
            @ApiImplicitParam(name = "interName", value = "接口名称", required = false),
            @ApiImplicitParam(name = "paramName", value = "参数类型", required = false),
    })
    @PostMapping("/param/list")
    public Result selectInterParam(@ApiIgnore Page<InterParams> page,Integer interId, String interName, String paramName) {
        Page<InterParams> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<InterParams>  list=interParamsMapper.selectInterParamList(jpage,interId,interName,paramName);
        jpage.setRecords(list);
        jpage.setTotal(interParamsMapper.selectCount(interId,interName,paramName));
        return Result.of(jpage);
    }

    @ApiOperation(value = "r增加报告模板")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportModel.class),
    })
    @PostMapping("/model/add")
    public Result addModelInfo(ReportModel reportModel) {
        String pathFile= null;
        try {
            pathFile = ftpFileService.upload(reportModel.getModelFile().getOriginalFilename(),reportModel.getModelFile().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reportModel.setModelFileUrl(pathFile);
        return Result.of(reportModelMapper.insert(reportModel));
    }
    @ApiOperation(value = "r删除报告模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报告模板ID", required = true),
    })
    @PostMapping("/model/del")
    public Result delModel(Integer id) {
        return Result.of(reportModelMapper.deleteById(id));
    }
    @ApiOperation(value = "r编辑报告模板")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportModel.class),
    })
    @PostMapping("/model/edit")
    public Result editModelInfo(ReportModel reportModel) {
        String pathFile= null;
        //如果又上传了模板文件
        if (reportModel!=null && reportModel.getModelFile()!=null) {
            try {
                pathFile = ftpFileService.upload(reportModel.getModelFile().getOriginalFilename(), reportModel.getModelFile().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            reportModel.setModelFileUrl(pathFile);
            //修改模板
            reportModelMapper.insert(reportModel);
            //删除原来服务器上的文件
            ftpFileService.delete(reportModel.getModelFileUrl());
        }
        return Result.ok();
    }

    @ApiOperation(value = "p增加模板接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板id", required = true),
            @ApiImplicitParam(name = "idsList", value = "接口ID数组", required = true),
    })
    @PostMapping("/model/addinter")
    public Result addModelInter(IdlistVo idlistVo) {
        if (idlistVo==null) {
            return Result.error(ResultCode.NOT_FOUND);
        }

        List<String> msglist = null;
        Integer[] idsList = idlistVo.getIdsList();
        for (Integer id : idsList) {
            if (id == null || id <= 0) {
                continue;
            }

            // 更新报告状态
            ModelInterfaceRel rel = new ModelInterfaceRel();
            rel.setModelId(idlistVo.getId());
            rel.setInterId(id);
            modelInterfaceRelMapper.insert(rel);
        }
        return Result.ok();
    }
    @ApiOperation(value = "pm增加模板接口参数映射")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportParamsMap.class),
    })
    @PostMapping("/interparammap/add")
    public Result addModelInterParamMap(ReportParamsMap reportParamsMap) {
        return Result.of(reportParamsMapMapper.insert(reportParamsMap));
    }

    @ApiOperation(value = "pm删除模板接口参数映射")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "映射id", required = true),
    })
    @PostMapping("/model/delinterparammap")
    public Result delModelInterParamMap(Integer id) {
        return Result.of(reportParamsMapMapper.deleteById(id));
    }

    @ApiOperation(value = "pm修改模板接口参数映射")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportParamsMap.class),
    })
    @PostMapping("/interparammap/edit")
    public Result editModelInterParamMap(ReportParamsMap reportParamsMap) {
        return Result.of(reportParamsMapMapper.updateById(reportParamsMap));
    }

    @ApiOperation(value = "pm批量删除模板接口参数映射")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板id", required = true),
            @ApiImplicitParam(name = "idsList", value = "接口映射参数ID数组", required = true),
    })
    @PostMapping("/interparammap/patchdel")
    public Result pdelModelInterParamMap(IdlistVo idlistVo) {
        for (Integer id : idlistVo.getIdsList()) {
            if (id == null || id <= 0) {
                continue;
            }
            modelInterfaceRelMapper.deleteById(id);
        }
        return Result.ok();
    }
    @ApiOperation(value = "pm模板接口参数映射列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "interId", value = "接口Id", required = true),
            @ApiImplicitParam(name = "interName", value = "接口名称", required = false),
            @ApiImplicitParam(name = "paramName", value = "参数类型", required = false),
    })
    @PostMapping("/interparammap/list")
    public Result selectInterParamMap(@ApiIgnore Page<ModelInterfaceRelListVo> page, Integer interId, String interName, String paramName) {
        Page<InterParams> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<InterParams>  list=interParamsMapper.selectInterParamList(jpage,interId,interName,paramName);
        jpage.setRecords(list);
        jpage.setTotal(interParamsMapper.selectCount(interId,interName,paramName));
        return Result.of(jpage);
    }
}
