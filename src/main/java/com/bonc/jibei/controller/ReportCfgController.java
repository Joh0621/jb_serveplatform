package com.bonc.jibei.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.EnumValue;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.api.ResultCode;
import com.bonc.jibei.entity.*;
import com.bonc.jibei.mapper.*;
import com.bonc.jibei.service.FileService;

import com.bonc.jibei.vo.*;

import io.swagger.annotations.*;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    StationTypeMapper stationTypeMapper;

    @Resource
    StationMapper stationMapper;

    @Resource
    StationModelRelMapper stationModelRelMapper;

    @Resource
    FileService ftpFileService;


    @ApiOperation(value = "报告脚本定义_增加接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @PostMapping("/inter/add")
    public Result addInter(@RequestBody ReportInterface reportInterface) {
        return Result.of(reportInterfaceMapper.insert(reportInterface));
    }

    @ApiOperation(value = "报告脚本定义_删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口ID", required = true),
    })
    @PostMapping("/inter/del")
    public Result delInter(Integer id) {
        return Result.of(reportInterfaceMapper.deleteById(id));
    }


    @ApiOperation(value = "报告脚本定义_接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "interType", value = "接口类型", required = false),
    })
    @PostMapping("/inter/select")
    public Result selectInter(@ApiIgnore Page<ReportInterface> page,  String interType) {
        Page<ReportInterface> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ReportInterface>  list=reportInterfaceMapper.selectReportInterList(jpage,interType);
        jpage.setRecords(list);
        jpage.setTotal(reportInterfaceMapper.selectCount(interType));
        return Result.of(jpage);
    }

    @ApiOperation(value = "报告模板配置_接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "modelName", value = "名称", required = false),
            @ApiImplicitParam(name = "interType", value = "接口类型", required = false),
            @ApiImplicitParam(name = "modelId", value = "模板Id", required = false),
    })
    @PostMapping("/inter/select")
    public Result selectModelInter(@ApiIgnore Page<ModelInterfaceRelListVo> page, String modelName, String interType,Integer modelId) {
        Page<ModelInterfaceRelListVo> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ModelInterfaceRelListVo>  list=reportInterfaceMapper.selectReportModelInterList(jpage,modelName,interType,modelId);
        jpage.setRecords(list);
        jpage.setTotal(reportInterfaceMapper.selectModelInterCount(modelName,interType,modelId));
        return Result.of(jpage);
    }


    @ApiOperation(value = "报告脚本定义_根据接口编码取接口数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @GetMapping("/inter/interbycode")
    public Result selectInterByCode(String code) {
        if (code==null || "".equals(code)) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        QueryWrapper<ReportInterface> qw=new QueryWrapper();
        qw.eq("code",code);
        List<ReportInterface> l=reportInterfaceMapper.selectList(qw);
        if (l!=null && l.size()>0){
            return Result.of(l.get(0));
        }
        return Result.of(l);
    }

    @ApiOperation(value = "报告脚本定义_接口类型下拉框")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = KeyValueVO.class),
    })
    @GetMapping("/inter/intertype")
    public Result selectInterType() {
        return Result.of(EnumValue.getUserTypeName());
    }

    @ApiOperation(value = "报告脚本定义_增加接口参数")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @PostMapping("/param/add")
    public Result addInterParam(@RequestBody InterParams interParams) {
//        interParams.setInterId(1);
        if (interParams==null) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        return Result.of(interParamsMapper.insert(interParams));
    }

    @ApiOperation(value = "报告脚本定义_删除接口参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "参数ID", required = true),
    })
    @PostMapping("/param/del")
    public Result delInterParam(Integer id) {
        return Result.of(interParamsMapper.deleteById(id));
    }

    @ApiOperation(value = "报告脚本定义_接口参数列表")
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

    @ApiOperation(value = "报告模板配置_模板类型下拉框")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = StationType.class),
    })
    @PostMapping("/model/typelist")
    public Result typelist() {
        QueryWrapper<StationType> qw=new QueryWrapper<>();
        qw.eq("is_show",1);
        List<StationType> list=stationTypeMapper.selectList(qw);

        List<KeyValueVO> vo= new ArrayList<>();
        list.forEach(p->{
            KeyValueVO v=new KeyValueVO();
            v.setKey(p.getId().toString());
            v.setValue(p.getTypeName());
            vo.add(v);
        });
        return Result.of(vo);
    }

    @ApiOperation(value = "报告模板配置_模板|报告状态下拉框")
    @PostMapping("/model/statuslist")
    public Result statuslist() {
        return Result.of(EnumValue.getReportStatus());
    }

    @ApiOperation(value = "报告模板配置_删除报告模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报告模板ID", required = true),
    })
    @PostMapping("/model/del")
    public Result delModel(Integer id) {
        return Result.of(reportModelMapper.deleteById(id));
    }
    @ApiOperation(value = "报告模板配置_编辑报告模板")
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

    @ApiOperation(value = "报告模板配置_参数配置_增加模板接口")
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
    @ApiOperation(value = "报告模板配置_参数配置_增加模板接口参数映射")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportParamsMap.class),
    })
    @PostMapping("/interparammap/add")
    public Result addModelInterParamMap(ReportParamsMap reportParamsMap) {
        return Result.of(reportParamsMapMapper.insert(reportParamsMap));
    }

    @ApiOperation(value = "报告模板配置_参数配置_删除模板接口参数映射")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "映射id", required = true),
    })
    @PostMapping("/model/delinterparammap")
    public Result delModelInterParamMap(Integer id) {
        return Result.of(reportParamsMapMapper.deleteById(id));
    }

    @ApiOperation(value = "报告模板配置_参数配置_修改模板接口参数映射")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportParamsMap.class),
    })
    @PostMapping("/interparammap/edit")
    public Result editModelInterParamMap(ReportParamsMap reportParamsMap) {
        return Result.of(reportParamsMapMapper.updateById(reportParamsMap));
    }

    @ApiOperation(value = "报告模板配置_参数配置_批量删除模板接口参数映射")
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
    @ApiOperation(value = "报告模板配置_参数配置_模板接口参数映射列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "interCode", value = "接口编码", required = false),
            @ApiImplicitParam(name = "paramCode", value = "参数编码", required = false),
            @ApiImplicitParam(name = "paramName", value = "参数名称", required = false),
    })
    @PostMapping("/interparammap/list")
    public Result selectInterParamMap(@ApiIgnore Page<ModelInterParamMapVo> page, String interCode, String paramCode, String paramName) {
        Page<ModelInterParamMapVo> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ModelInterParamMapVo>  list=reportParamsMapMapper.selectReportParamsMapList(jpage,interCode,paramCode,paramName);
        jpage.setRecords(list);
        jpage.setTotal(reportParamsMapMapper.selectCount(interCode,paramCode,paramName));
        return Result.of(jpage);
    }

    @ApiOperation(value = "报告配置_场站列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @PostMapping("/reportcfg/stationlist")
    public Result stationlist() {
        QueryWrapper<Station> qw=new QueryWrapper<>();
        List<Station> list=stationMapper.selectList(qw);
        return Result.of(list);
    }

    @ApiOperation(value = "报告配置_报告名称下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelName", value = "模板名称", required = false),
            @ApiImplicitParam(name = "modelv", value = "模板版本号", required = false),
            @ApiImplicitParam(name = "reportType", value = "报告类型", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = KeyValueVO.class),
    })
    @GetMapping("/reportcfg/reportnamelist")
    public Result reportNamelist(String modelName,String modelv,String reportType) {
        //找模板ID
        QueryWrapper<ReportModel> qw=new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(modelName),"model_name",modelName);
        qw.eq(StrUtil.isNotBlank(modelv),"model_version",modelv);
        qw.eq(StrUtil.isNotBlank(reportType),"model_type",reportType);
        List<ReportModel> list=reportModelMapper.selectList(qw);
        List<KeyValueVO> vo= new ArrayList<>();
        if (list!=null && list.size()>0){
            list.forEach(p->{
                KeyValueVO v=new KeyValueVO();
                v.setKey(p.getReportName());
                v.setValue(p.getReportName());
                vo.add(v);
            });
        }
        //以后判断 key 唯一性
        return Result.of(vo);
    }

    @ApiOperation(value = "报告配置_模板名称下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportName", value = "报告名称", required = false),
            @ApiImplicitParam(name = "modelv", value = "模板版本号", required = false),
            @ApiImplicitParam(name = "reportType", value = "报告类型", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = KeyValueVO.class),
    })
    @GetMapping("/reportcfg/modelnamelist")
    public Result modelNamelist(String reportName,String modelv,String reportType) {
        //找模板ID
        QueryWrapper<ReportModel> qw=new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(reportName),"report_name",reportName);
        qw.eq(StrUtil.isNotBlank(modelv),"model_version",modelv);
        qw.eq(StrUtil.isNotBlank(reportType),"model_type",reportType);
        List<ReportModel> list=reportModelMapper.selectList(qw);
        List<KeyValueVO> vo= new ArrayList<>();
        if (list!=null && list.size()>0){
            //下拉框
            list.forEach(p->{
                KeyValueVO v=new KeyValueVO();
                v.setKey(p.getModelName());
                v.setValue(p.getModelName());
                vo.add(v);
            });
        }
        //以后判断 key 唯一性
        return Result.of(vo);
    }

    @ApiOperation(value = "报告配置_模板版本号下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportName", value = "报告名称", required = false),
            @ApiImplicitParam(name = "modelName", value = "模板名称", required = false),
            @ApiImplicitParam(name = "reportType", value = "报告类型", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = KeyValueVO.class),
    })
    @GetMapping("/reportcfg/modelvlist")
    public Result reportTypelist(String reportName,String modelName,String reportType) {
        //找模板ID
        QueryWrapper<ReportModel> qw=new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(reportName),"report_name",reportName);
        qw.eq(StrUtil.isNotBlank(modelName),"model_name",modelName);
        qw.eq(StrUtil.isNotBlank(reportType),"model_type",reportType);
        List<ReportModel> list=reportModelMapper.selectList(qw);
        List<KeyValueVO> vo= new ArrayList<>();
        if (list!=null && list.size()>0){
            //下拉框
            list.forEach(p->{
                KeyValueVO v=new KeyValueVO();
                v.setKey(p.getModelName());
                v.setValue(p.getModelName());
                vo.add(v);
            });
        }
        return Result.of(vo);
    }

    @ApiOperation(value = "报告配置_报告类型下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportName", value = "报告名称", required = false),
            @ApiImplicitParam(name = "modelName", value = "模板名称", required = false),
            @ApiImplicitParam(name = "modelv", value = "模板版本号", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = KeyValueVO.class),
    })
    @GetMapping("/reportcfg/modelvreportTypelist")
    public Result modelVersionlist(String reportName,String modelv,String modelName) {
        //找模板ID
        QueryWrapper<ReportModel> qw=new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(reportName),"report_name",reportName);
        qw.eq(StrUtil.isNotBlank(modelv),"model_version",modelv);
        qw.eq(StrUtil.isNotBlank(modelName),"model_name",modelName);
        List<ReportModel> list=reportModelMapper.selectList(qw);
        List<KeyValueVO> vo= new ArrayList<>();
        if (list!=null && list.size()>0){
            //下拉框
            list.forEach(p->{
                KeyValueVO v=new KeyValueVO();
                v.setKey(p.getModelName());
                v.setValue(p.getModelName());
                vo.add(v);
            });
        }
        //以后判断 key 唯一性
        return Result.of(vo);
    }

    @ApiOperation(value = "报告配置_新建场站报告_添加场站")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ModelStationIdsVo.class),
    })
    @PostMapping("/reportcfg/addstation")
    public Result addStationReport(ModelStationIdsVo modelStationIdsVo) {
        //找模板ID
        String reportName=modelStationIdsVo.getReportName();
        String reportType=modelStationIdsVo.getReportType();
        String modelv=modelStationIdsVo.getModelv();
        String modelName=modelStationIdsVo.getModelName();
        QueryWrapper<ReportModel> qw=new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(reportName),"report_name",reportName);
        qw.eq(StrUtil.isNotBlank(modelv),"model_version",modelv);
        qw.eq(StrUtil.isNotBlank(reportType),"model_type",reportType);
        qw.eq(StrUtil.isNotBlank(modelName),"model_name",modelName);
        List<ReportModel> list=reportModelMapper.selectList(qw);
        if (list!=null && list.size()>0){
            Integer mid=list.get(0).getId();//模板ID
            //添加配置报告


            Integer[] ids=modelStationIdsVo.getIdList();
            if (ids!=null){
                for (Integer id:ids){
                    StationModelRel rel=new StationModelRel();
                    rel.setModelId(mid);
                    rel.setStationId(id);
                    rel.setCreateTime(LocalDateTime.now());
                    stationModelRelMapper.insert(rel);
                }
            }
        }
        //以后判断 key 唯一性
        return Result.of(list);
    }

    @ApiOperation(value = "报告配置_新建场站报告_编辑场站(报告名称、报告类型、选择模板、模板版本号不可编辑)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ModelStationIdsVo.class),
    })
    @PostMapping("/reportcfg/editstation")
    public Result editStationReport(ModelStationIdsVo modelStationIdsVo) {
        //找模板ID
        /*
        String reportName=modelStationIdsVo.getReportName();
        String reportType=modelStationIdsVo.getReportType();
        String modelv=modelStationIdsVo.getModelv();
        String modelName=modelStationIdsVo.getModelName();
        QueryWrapper<ReportModel> qw=new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(reportName),"report_name",reportName);
        qw.eq(StrUtil.isNotBlank(modelv),"model_version",modelv);
        qw.eq(StrUtil.isNotBlank(reportType),"model_type",reportType);
        qw.eq(StrUtil.isNotBlank(modelName),"model_name",modelName);
        List<ReportModel> list=reportModelMapper.selectList(qw);
        */
        //先删除以前的模板场站
        QueryWrapper<StationModelRel> qw=new QueryWrapper();
        qw.eq("model_id",modelStationIdsVo.getModelId());
        stationModelRelMapper.delete(qw);

        Integer[] islist=modelStationIdsVo.getIdList();
        //再增加新的场站
        if (islist!=null && islist.length>0){
            for (Integer id:islist){
                 StationModelRel rel=new StationModelRel();
                 rel.setModelId(modelStationIdsVo.getModelId());
                 rel.setStationId(id);
                 rel.setCreateTime(LocalDateTime.now());
                 stationModelRelMapper.insert(rel);
            }
        }
        return Result.ok();
    }

    @ApiOperation(value = "报告配置_删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板ID", required = false),
    })
    @PostMapping("/reportcfg/delmodelcfgs")
    public Result delmodelcfgs(Integer id) {
        QueryWrapper<StationModelRel> rel=new QueryWrapper<>();
        rel.eq("model_id",id);
        return Result.of(stationModelRelMapper.delete(rel));
    }

    @ApiOperation(value = "报告配置_启用|停用")

    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "模板ID", required = true),
        @ApiImplicitParam(name = "status", value = "2=已停用，1= 启用.0=待启用", required = true),
    })
    @PostMapping("/reportcfg/reportstatus")
    public Result statuslmodelcfgs(Integer id,Integer status) {
        ReportModel m=new ReportModel();
        m.setId(id);
        m.setReportStatus(status);
        m.setReportStatusDate(LocalDate.now());
        return Result.of(reportModelMapper.insert(m));
    }

    @ApiOperation(value = "报告配置_列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "modelType", value = "模板类型", required = true),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态;2=已停用，1= 启用.0=待启用", required = false),
            @ApiImplicitParam(name = "name", value = "模板名称|报告名称", required = false),
    })
    @PostMapping("/reportcfg/list")
    public Result reportcfgList(@ApiIgnore Page<InterParams> page,Integer modelType, Integer reportStatus, String name) {
        Page<ReportModel> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ReportModel> list=reportModelMapper.selectModelReportList(jpage,modelType,reportStatus,name);
        jpage.setRecords(list);
        jpage.setTotal(reportModelMapper.reportSelectCount(modelType,reportStatus,name));
        return Result.of(jpage);
    }

    @ApiOperation(value = "报告配置_详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板id", required = true),
    })
    @PostMapping("/reportcfg/view")
    public Result reportcfgView(Integer id) {
        ReportModel m=reportModelMapper.selectById(id);
        if (m==null){
            return Result.error(ResultCode.NOT_FOUND);
        }
        ModelReportViewVo vo=new ModelReportViewVo();
        vo.setReportName(m.getReportName());
        vo.setModelName(m.getModelName());
        vo.setModelv(m.getModelVersion());
        if (m.getModelType()!=null) {
            vo.setReportType(m.getModelType().toString());
        }
        vo.setRel(stationModelRelMapper.selectStationModelRelVoList(id));
        return Result.of(vo);
    }
}
