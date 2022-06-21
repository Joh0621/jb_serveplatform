package com.bonc.jibei.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.*;
import com.bonc.jibei.entity.*;
import com.bonc.jibei.mapper.*;
import com.bonc.jibei.service.FileService;
import com.bonc.jibei.vo.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/17 16:24
 * @Description: TODO
 */
@Api(tags = "报告配置接口")
@RestController
public class ReportCfgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @Resource
    ReportModelMapper reportModelMapper;

    @Resource
    ReportInterfaceMapper reportInterfaceMapper;

    @Resource
    InterParamsMapper interParamsMapper;

    @Resource
    ModelInterfaceRelMapper modelInterfaceRelMapper;

    @Resource
    ReportModelInterMapper reportModelInterMapper;


    @Resource
    ReporCfggMapper reporCfggMapper;


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

    @Value("${spring.cfg.modelPath}")
    private String modelFilePath;

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
    public Result selectInter(@ApiIgnore Page<ReportInterface> page, String interType) {

        Page<ReportInterface> jpage = new Page<>(page.getCurrent(), page.getSize());

        jpage.setSearchCount(false);
        List<ReportInterface> list;
        if (page.getCurrent() == 0 && page.getSize() == 0) {
            list = reportInterfaceMapper.selectReportInterList(null, interType);
        } else {
            list = reportInterfaceMapper.selectReportInterList(jpage, interType);
        }
        for (ReportInterface e : list) {
            switch (e.getInterType()) {
                case 1:
                    e.setInterTypeName(InterEnum.INTER_TYPE_NORMAL.getName());
                    break;
                case 2:
                    e.setInterTypeName(InterEnum.INTER_TYPE_TABLE.getName());
                    break;
                case 3:
                    e.setInterTypeName(InterEnum.INTER_TYPE_BAR.getName());
                    break;
                case 4:
                    e.setInterTypeName(InterEnum.INTER_TYPE_PIE.getName());
                    break;
                case 5:
                    e.setInterTypeName(InterEnum.INTER_TYPE_LINE.getName());
                    break;
                case 6:
                    e.setInterTypeName(InterEnum.INTER_TYPE_RADAR.getName());
                    break;
                case 7:
                    e.setInterTypeName(InterEnum.INTER_TYPE_BARS.getName());
                    break;
                case 8:
                    e.setInterTypeName(InterEnum.INTER_TYPE_STACKEDBARE.getName());
                    break;
                case 11:
                    e.setInterTypeName(InterEnum.INTER_TYPE_MIX.getName());
                    break;

            }

        }
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
    @PostMapping("/interparammap/select")
    public Result selectModelInter(@ApiIgnore Page<ModelInterfaceRelListVo> page, String modelName, String interType, Integer modelId) {
        Page<ModelInterfaceRelListVo> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ModelInterfaceRelListVo> list = reportInterfaceMapper.selectReportModelInterList(jpage, modelName, interType, modelId);
        jpage.setRecords(list);
        jpage.setTotal(reportInterfaceMapper.selectModelInterCount(modelName, interType, modelId));
        return Result.of(jpage);
    }


    @ApiOperation(value = "报告脚本定义_根据接口编码取接口数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @GetMapping("/inter/interbycode")
    public Result selectInterByCode(Integer code) {
        if (code == null || "".equals(code)) {
            return Result.error(ResultCode.NOT_FOUND);
        }
//        QueryWrapper<ReportInterface> qw = new QueryWrapper();
//        qw.eq("code", code);
//        List<ReportInterface> l = reportInterfaceMapper.selectList(qw);
        List<ModelInterfaceRelListVo> l = reportInterfaceMapper.selectReportModelByInterId(code);
        if (l != null && l.size() > 0) {
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
        if (interParams == null || "".equals(interParams.getParamName())) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        return Result.of(interParamsMapper.insert(interParams));
    }

    @ApiOperation(value = "报告脚本定义_修改接口参数")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportInterface.class),
    })
    @PostMapping("/param/edit")
    public Result editInterParam(@RequestBody InterParams interParams) {
//        interParams.setInterId(1);
        if (interParams == null || "".equals(interParams.getParamName())) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        return Result.of(interParamsMapper.updateById(interParams));
    }

    @ApiOperation(value = "报告脚本定义_删除接口参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "参数ID", required = true),
    })
    @PostMapping("/param/del")
    public Result delInterParam(Integer id) {
        return Result.of(interParamsMapper.deleteById(id));
    }


    @ApiOperation(value = "报告脚本定义批量删除接口参数映射")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsList", value = "接口映射参数ID数组", required = true),
    })
    @PostMapping("/param/patchdel")
    public Result patchdelInterParam(@RequestBody IdlistVo idlistVo) {
        for (Integer id : idlistVo.getIdsList()) {
            if (id == null || id <= 0) {
                continue;
            }
            interParamsMapper.deleteById(id);
        }
        return Result.ok();
    }

    @ApiOperation(value = "报告脚本定义_接口参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = false),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = false),
            @ApiImplicitParam(name = "interId", value = "接口Id", required = true),
            @ApiImplicitParam(name = "interName", value = "接口名称", required = false),
            @ApiImplicitParam(name = "paramName", value = "参数类型", required = false),
    })
    @PostMapping("/param/list")
    public Result selectInterParam(@ApiIgnore Page<InterParams> page, Integer interId, String interName, String paramName) {
        Page<ModelInterParamMapVo> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);

        List<ModelInterParamMapVo> list;
        if (page.getCurrent() == 0 && page.getSize() == 0) {
            list = interParamsMapper.selectInterParamList(null, interId, interName, paramName);
        } else {
            list = interParamsMapper.selectInterParamList(jpage, interId, interName, paramName);
        }

        for (ModelInterParamMapVo e : list) {
            switch (e.getParamType()) {
                case 1:
                    e.setParamTypeName(InterEnum.INTER_TYPE_NORMAL.getName());
                    break;
                case 2:
                    e.setParamTypeName(InterEnum.INTER_TYPE_TABLE.getName());
                    break;
                case 3:
                    e.setParamTypeName(InterEnum.INTER_TYPE_BAR.getName());
                    break;
                case 4:
                    e.setParamTypeName(InterEnum.INTER_TYPE_PIE.getName());
                    break;
                case 5:
                    e.setParamTypeName(InterEnum.INTER_TYPE_LINE.getName());
                    break;
                case 6:
                    e.setParamTypeName(InterEnum.INTER_TYPE_RADAR.getName());
                    break;
                case 7:
                    e.setParamTypeName(InterEnum.INTER_TYPE_BARS.getName());
                    break;
                case 8:
                    e.setParamTypeName(InterEnum.INTER_TYPE_STACKEDBARE.getName());
                    break;
                case 11:
                    e.setParamTypeName(InterEnum.INTER_TYPE_MIX.getName());
                    break;

            }
        }
        jpage.setRecords(list);
        jpage.setTotal(interParamsMapper.selectCount(interId, interName, paramName));
        return Result.of(jpage);
    }

    @ApiOperation(value = "报告模板配置_模板类型下拉框")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = StationType.class),
    })
    @PostMapping("/model/typelist")
    public Result typelist() {
        QueryWrapper<StationType> qw = new QueryWrapper<>();
        qw.eq("is_show", 1);
        List<StationType> list = stationTypeMapper.selectList(qw);

        List<KeyValueVO> vo = new ArrayList<>();
        list.forEach(p -> {
            KeyValueVO v = new KeyValueVO();
            v.setKey(p.getId().toString());
            v.setValue(p.getTypeName());
            vo.add(v);
        });
        return Result.of(vo);
    }

//    @ApiOperation(value = "报告模板配置_报告名称下拉框")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "OK", response = StationType.class),
//    })
//    @PostMapping("/model/reportnamelist")
//    public Result reportnamelist() {
//
//        List<ReportModel> list = reportModelMapper.selectReportList();
//        List<String> vo= new ArrayList<>();
//        list.forEach(p->{
////            KeyValueVO v=new KeyValueVO();
////            v.setKey(p.getId().toString());
////            v.setValue(p.getTypeName());
//            vo.add( p.getReportName());
//        });
//        return Result.of(vo);
//    }

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

//    @ApiOperation(value = "报告模板配置_修改报告模板")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "报告模板ID", required = true),
//    })
//    @PostMapping("/model/edit")
//    public Result editModel(ReportModel reportModel)  {
//        return Result.of(reportModelMapper.updateById(reportModel));
//    }


    @ApiOperation(value = "报告模板配置_新增基本信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportModel.class),
    })
    @PostMapping("/model/addbaseinfo")
    public Result modelAddBaseInfo(@RequestBody ReportModel reportModel) {
        reportModel.setStatus(Integer.valueOf(StatusEnum.INTER_TYPE_NOCHK.getCode()));
        reportModel.setOperStatusDate(LocalDate.now());
        reportModelMapper.insert(reportModel);
        List<ReportModel> reportModels = reportModelMapper.selectModelReportList(null, reportModel.getModelType(), null, reportModel.getModelName());
        return Result.of(reportModels);
    }

    @ApiOperation(value = "报告模板配置_详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板id", required = true),
    })
    @PostMapping("/model/view")
    public Result modelView(Integer id) {
//        ReportModel m=reportModelMapper.selectById(id);
        if (id == null) {
            return Result.error(ResultCode.NOT_FOUND);
        }

//        ModelInterfaceRelListVo vo = new ModelInterfaceRelListVo();

        List<ModelInterfaceRelListVo> infoByModelIds = reportModelMapper.selectInfoByModelId(id);

//        vo.setRel(stationModelRelMapper.selectStationModelRelVoList(id));
//        BeanUtils.copyProperties(vo, m);


        return Result.of(infoByModelIds);

    }

    @ApiOperation(value = "新增报告模板配置_编辑报告模板")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ReportModel.class),
    })
    @PostMapping("/model/edit")
    public Result editModelInfo(@RequestBody ReportModel reportModel) {
        String pathFile = null;
        if (reportModel == null) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        //如果又上传了模板文件
        if (reportModel != null && reportModel.getModelFile() != null) {
            try {
                pathFile = ftpFileService.upload(reportModel.getModelFile().getOriginalFilename(), reportModel.getModelFile().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.info(pathFile);
            reportModel.setModelFileUrl(pathFile);
            //修改模板
            reportModelMapper.insert(reportModel);
            //删除原来服务器上的文件
//            ftpFileService.delete(reportModel.getModelFileUrl());
        } else {
            reportModelMapper.updateById(reportModel);
        }
        return Result.ok();
    }

    @ApiOperation(value = "报告模板配置_启用|停用")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板ID", required = true),
            @ApiImplicitParam(name = "status", value = "2=已停用，1= 启用.0=待启用", required = true),
    })
    @PostMapping("/model/modelstatus")
    public Result modelstatusedit(Integer id, Integer status) {
        ReportModel m = new ReportModel();
        m.setId(id);
        m.setStatus(status);
        m.setOperStatusDate(LocalDate.now());
        return Result.of(reportModelMapper.updateById(m));
    }

    /**
     * TODO 待确定具体模板定义，暂时计划模板-报告为一对多关系
     *
     * @param page
     * @param modelType
     * @param modelStatus
     * @param name
     * @return
     */
    @ApiOperation(value = "报告模板配置_列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "modelType", value = "模板类型", required = false),
            @ApiImplicitParam(name = "modelStatus", value = "模板状态;2=已停用，1= 启用.0=待启用", required = false),
            @ApiImplicitParam(name = "name", value = "模板名称|报告名称", required = false),
    })
    @PostMapping("/model/list")
    public Result selectModelList(@ApiIgnore Page<InterParams> page, Integer modelType, Integer modelStatus, String name) {
        Page<ReportModel> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ReportModel> list = reportModelMapper.selectModelReportList(jpage, modelType, modelStatus, name);
        jpage.setRecords(list);
        jpage.setTotal(reportModelMapper.reportSelectCount(modelType, modelStatus, name));
        return Result.of(jpage);
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
        if (idlistVo == null) {
            return Result.error(ResultCode.NOT_FOUND);
        }

//        List<String> msglist = null;
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
    @ResponseBody
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
    public Result editModelInterParamMap(@RequestBody ReportParamsMap reportParamsMap) {
        return Result.of(reportParamsMapMapper.updateById(reportParamsMap));
    }

    @ApiOperation(value = "报告模板配置_参数配置_批量删除模板接口参数映射")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "模板id", required = true),
            @ApiImplicitParam(name = "idsList", value = "接口映射参数ID数组", required = true),
    })
    @PostMapping("/interparammap/patchdel")
    public Result pdelModelInterParamMap(@RequestBody IdlistVo idlistVo) {
        for (Integer id : idlistVo.getIdsList()) {
            if (id == null || id <= 0) {
                continue;
            }
            reportParamsMapMapper.deleteById(id);
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
        List<ModelInterParamMapVo> list = reportParamsMapMapper.selectReportParamsMapList(jpage, interCode, paramCode, paramName);
        jpage.setRecords(list);
        jpage.setTotal(reportParamsMapMapper.selectCount(interCode, paramCode, paramName));
        return Result.of(jpage);
    }

    @ApiOperation(value = "报告模版配置_文件上传")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @PostMapping("/model/uploadFile")
    @ResponseBody
    public Result upload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String pathFile = ftpFileService.upload(filename, file.getInputStream());
        LOGGER.info(pathFile);
        //文件存放的路径
//        File file1 = new File(modelFilePath + filename);
//        file.transferTo(file1);
        String[] split = pathFile.split("/");
        Map<String, String> map = new HashMap<>();
        map.put("filename", filename);
        map.put("filepath", split[split.length - 1]);
        return Result.ok(map);
    }


    @ApiOperation(value = "报告模版配置_文件下载")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @GetMapping("/model/downFile")
    public String fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName) {
        File file = new File(modelFilePath + fileName);
//        System.out.println(file);
        if (!file.exists()) {
            return "下载文件不存在";
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return "下载失败";
        }
        return "下载成功";
    }

    @ApiOperation(value = "报告模版配置_文件删除")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @GetMapping("model/deletFile")
    @ResponseBody
    public Result deletFile(@RequestParam("fileName") String fileName) {
        String filepathDir = modelFilePath + fileName;
        FileSystemUtils.deleteRecursively(new File(filepathDir + fileName));
        return Result.ok();


    }

    @ApiOperation(value = "报告配置_场站列表")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = IdlistVo.class),
    })
    @PostMapping("/reportcfg/stationlist")
    public Result stationlist() {
        QueryWrapper<Station> qw = new QueryWrapper<>();
        List<Station> list = stationMapper.selectList(qw);
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
    public Result reportNamelist(String modelName, String modelv, String reportType) {
        //找模板ID
        QueryWrapper<ReportModel> qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(modelName), "model_name", modelName);
        qw.eq(StrUtil.isNotBlank(modelv), "model_version", modelv);
        qw.eq(StrUtil.isNotBlank(reportType), "model_type", reportType);
        List<ReportModel> list = reportModelMapper.selectList(qw);
        List<KeyValueVO> vo = new ArrayList<>();
        if (list != null && list.size() > 0) {
            list.forEach(p -> {
                KeyValueVO v = new KeyValueVO();
//                v.setKey(p.getReportName());
//                v.setValue(p.getReportName());
                vo.add(v);
            });
        }
        //以后判断 key 唯一性
        return Result.of(vo);
    }

    @ApiOperation(value = "报告配置_模板名称下拉框")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "reportName", value = "报告名称", required = false),
//            @ApiImplicitParam(name = "modelv", value = "模板版本号", required = false),
//            @ApiImplicitParam(name = "reportType", value = "报告类型", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = KeyValueVO.class),
    })
    @GetMapping("/reportcfg/modelnamelist")
    public Result modelNamelist() {
        //找模板ID
        List<ReportModel> list = reportModelMapper.selectModelReportList(null, null, null, null);

//        List<ReportModel> list = reportModelMapper.selectList(qw);
        List<KeyValueVO> vo = new ArrayList<>();
        if (list != null && list.size() > 0) {
            //下拉框
            list.forEach(p -> {
                KeyValueVO v = new KeyValueVO();
                if (StrUtil.isNotBlank(p.getModelName())) {
                    v.setKey(p.getModelName());
                    v.setValue(p.getModelVersion());
                    vo.add(v);
                }


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
    public Result reportTypelist(String reportName, String modelName, String reportType) {
        //找模板ID
        QueryWrapper<ReportModel> qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(reportName), "report_name", reportName);
        qw.eq(StrUtil.isNotBlank(modelName), "model_name", modelName);
        qw.eq(StrUtil.isNotBlank(reportType), "model_type", reportType);
        List<ReportModel> list = reportModelMapper.selectList(qw);
        List<KeyValueVO> vo = new ArrayList<>();
        if (list != null && list.size() > 0) {
            //下拉框
            list.forEach(p -> {
                KeyValueVO v = new KeyValueVO();
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
    public Result modelVersionlist(String reportName, String modelv, String modelName) {
        //找模板ID
        QueryWrapper<ReportModel> qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(reportName), "report_name", reportName);
        qw.eq(StrUtil.isNotBlank(modelv), "model_version", modelv);
        qw.eq(StrUtil.isNotBlank(modelName), "model_name", modelName);
        List<ReportModel> list = reportModelMapper.selectList(qw);
        List<KeyValueVO> vo = new ArrayList<>();
        if (list != null && list.size() > 0) {
            //下拉框
            list.forEach(p -> {
                KeyValueVO v = new KeyValueVO();
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
    public Result addStationReport(@RequestBody ModelStationIdsVo modelStationIdsVo) {
        //找模板ID
        String reportName = modelStationIdsVo.getReportName();
        String reportType = modelStationIdsVo.getReportType();
        String modelv = modelStationIdsVo.getModelVersion();
        String modelName = modelStationIdsVo.getModelName();
        QueryWrapper<ReportModel> qw = new QueryWrapper<>();
//        qw.eq(StrUtil.isNotBlank(reportName), "report_name", reportName);
        qw.eq(StrUtil.isNotBlank(modelv), "model_version", modelv);
//        qw.eq(StrUtil.isNotBlank(reportType), "model_type", reportType);
        qw.eq(StrUtil.isNotBlank(modelName), "model_name", modelName);
        List<ReportModel> list = reportModelMapper.selectList(qw);
        ReportCfg cfg = new ReportCfg();
        Integer modleId = list.get(0).getId();
        cfg.setModelId(modleId);
//        System.out.println(reportType);
        cfg.setReportType(Integer.valueOf(reportType));
        cfg.setReportName(reportName);
        cfg.setCreateTime(LocalDateTime.now());
        reporCfggMapper.insert(cfg);
        //找报告id
        QueryWrapper<ReportCfg> qwRp = new QueryWrapper<>();
        qwRp.eq(StrUtil.isNotBlank(modleId.toString()), "model_id", modleId);
        qwRp.eq(StrUtil.isNotBlank(reportType), "report_type", reportType);
        qwRp.eq(StrUtil.isNotBlank(reportName), "report_name", reportName);
        List<ReportCfg> listCfg = reporCfggMapper.selectList(qwRp);
        if (list != null && list.size() > 0) {
            Integer mid = list.get(0).getId();//模板ID
            //添加配置报告
            Integer[] ids = modelStationIdsVo.getIdList();
            if (ids != null) {
                for (Integer id : ids) {
                    StationModelRel rel = new StationModelRel();
                    rel.setCfgId(listCfg.get(0).getId());
                    rel.setStationId(id);
                    rel.setCreateTime(LocalDateTime.now());
                    stationModelRelMapper.insert(rel);


                }
            }
            return Result.of(Result.ok());
        } else {
            return Result.of(Result.error(ResultCode.NOT_FOUND));
        }
        //以后判断 key 唯一性

    }

    @ApiOperation(value = "报告配置_新建场站报告_编辑场站(报告名称、报告类型、选择模板、模板版本号不可编辑)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ModelStationIdsVo.class),
    })
    @PostMapping("/reportcfg/editstation")
    public Result editStationReport(@RequestBody ModelStationIdsVo modelStationIdsVo) {
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
        QueryWrapper<StationModelRel> qw = new QueryWrapper();
        qw.eq("cfg_id", modelStationIdsVo.getReportId());
        stationModelRelMapper.delete(qw);

        Integer[] islist = modelStationIdsVo.getIdList();
        //再增加新的场站
        if (islist != null && islist.length > 0) {
            for (Integer id : islist) {
                StationModelRel rel = new StationModelRel();
                rel.setCfgId(modelStationIdsVo.getReportId());
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
//        QueryWrapper<StationModelRel> rel = new QueryWrapper<>();
//        rel.eq("model_id", id);
//        return Result.of(stationModelRelMapper.delete(rel));
        return Result.of(reporCfggMapper.deleteById(id));

    }


    @ApiOperation(value = "报告配置_批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsList", value = "ID数组", required = true),
    })
    @PostMapping("/reportcfg/patchdelmodelcfgs")
    public Result patchdelmodelcfgs(@RequestBody IdlistVo idlistVo) {

        for (Integer stationId : idlistVo.getIdsList()) {
            if (stationId == null || idlistVo.getId() == null) {
                continue;
            }
            QueryWrapper<StationModelRel> rel = new QueryWrapper<>();
            rel.eq("station_id", stationId);
            rel.eq("cfg_id", idlistVo.getId());
            stationModelRelMapper.delete(rel);
        }

        return Result.of(Result.ok());

    }

    @ApiOperation(value = "报告配置_启用|停用")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板ID", required = true),
            @ApiImplicitParam(name = "status", value = "2=已停用，1= 启用.0=待启用", required = true),
    })
    @PostMapping("/reportcfg/reportstatus")
    public Result statuslmodelcfgs(Integer id, Integer status) {
        ReportCfg m = new ReportCfg();
        m.setId(id);
        m.setStatus(status);
//        m.setStartTime(LocalDateTime.from(LocalDate.now()));
        return Result.of(reporCfggMapper.updateById(m));
    }

    @ApiOperation(value = "报告配置_列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "modelType", value = "模板类型", required = false),
            @ApiImplicitParam(name = "reportStatus", value = "报告状态;2=已停用，1= 启用.0=待启用", required = false),
            @ApiImplicitParam(name = "name", value = "模板名称|报告名称", required = false),
    })
    @PostMapping("/reportcfg/list")
    public Result reportcfgList(@ApiIgnore Page<InterParams> page, Integer modelType, Integer reportStatus, String name) {
        Page<ReportModelVo> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<ReportModelVo> list = reportModelMapper.selectReportCfgList(jpage, modelType, reportStatus, name);
        jpage.setRecords(list);
        jpage.setTotal(reportModelMapper.reportCfgSelectCount(modelType, reportStatus, name));
        return Result.of(jpage);
    }

    @ApiOperation(value = "报告配置_详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板id", required = true),
    })
    @PostMapping("/reportcfg/view")
    @ResponseBody

    public Result reportcfgView(@RequestBody ReportModelVo reVo) {
        if (reVo == null) {
            return Result.error(ResultCode.NOT_FOUND);
        }

        ModelReportViewVo vo = new ModelReportViewVo();


        vo.setReportName(reVo.getReportName());
        vo.setModelName(reVo.getModelName());
        vo.setModelv(reVo.getModelVersion());
        if (reVo.getReportType() != null) {
            vo.setReportType(reVo.getReportType().toString());
        }
        StationModelRel rel = new StationModelRel();

        vo.setRel(stationModelRelMapper.selectStationModelRelVoList(reVo.getId()));

//        for (StationModelRelVo relVo:stationModelRelVo){
//
//        }
//        rel.setCfgId(mid);
//        rel.setStationId(id);
//        rel.setCreateTime(LocalDateTime.now());
//        stationModelRelMapper.insert(rel);
////        vo.setReportName(m.getReportName());
//        vo.setModelName(m.getModelName());
//        vo.setModelv(m.getModelVersion());
//        if (m.getModelType() != null) {
//            vo.setReportType(m.getModelType().toString());
//        }
//        vo.setRel(stationModelRelMapper.selectStationModelRelVoList(id));
        return Result.of(vo);
    }
}
