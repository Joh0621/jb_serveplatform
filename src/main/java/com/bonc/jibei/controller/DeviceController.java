package com.bonc.jibei.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.api.ResultCode;
import com.bonc.jibei.entity.*;
import com.bonc.jibei.mapper.DeviceTypeMapper;
import com.bonc.jibei.mapper.TreeMapper;
import com.bonc.jibei.vo.*;
import com.bonc.jibei.mapper.DeviceMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.stream.CollectorUtil.groupingBy;


@Api(tags = "台账配置接口")
@RestController
public class DeviceController {
    @Resource
    DeviceMapper deviceMapper;

    @Resource
    DeviceTypeMapper deviceTypeMapper;

    @Resource
    TreeMapper treeMapper;
    @ApiOperation(value = "设备基本信息配置_设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
    })
    @GetMapping("device/deviceInfoList")
    public Result deviceList(@ApiIgnore Page<Code> page,String codeId){
        Page<Code> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<Code> list = deviceMapper.selectDeviceList(jpage,codeId);
        jpage.setRecords(list);
        jpage.setTotal(deviceMapper.selectDeviceListCount(codeId));
        return Result.of(jpage);
    }
    @ApiOperation(value = "设备基本信息配置_参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设别类型id", required = true),
    })
    @GetMapping("device/deviceParamList")
    public Result deviceParamList(String id){
        if (id == null || "".equals(id)) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        List<CodeTypeVo> list = deviceMapper.selectdeviceParam(id);
        return Result.of(list);
    }

    @ApiOperation(value = "设备基本信息配置_参数新增/修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设别类型id", required = true),
    })
    @PostMapping("device/addDeviceParam")
    public Result addDeviceParam(@RequestBody CodeTypeVo vo){
        if (vo == null ||vo.getCodeName()=="") {
            return Result.error(ResultCode.NOT_FOUND);
        }else if (vo.getId()!=null){
            //修改数据
            CodeType codeType = new CodeType();
            BeanUtils.copyProperties(vo,codeType);
            System.out.println(codeType);
            return Result.of( deviceMapper.updateCodeType(codeType));
        }else {
            //插入新数据
            //插入codeType表并赋值返回codeid
            Integer codeId = deviceTypeMapper.selectByName(vo.getCodeName());
            int i = deviceMapper.insetCodeType(vo);
            Code code = new Code();
//            if (i==0){
//                return Result.error(ResultCode.FAILED);
//            }
            BeanUtils.copyProperties(vo,code);
            code.setPid(vo.getPid());
            code.setCodeId(vo.getCodeId()==null?codeId:vo.getCodeId());
            System.out.println(code);
            return Result.of(deviceMapper.insert(code));
        }
//        return Result.of(null);
    }

    @ApiOperation(value = "设备基本信息配置_参数删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codeId", value = "设别类型参数id", required = true),
    })
    @PostMapping("device/delDeviceParam")
    public Result delDeviceParam( String id,String codeId){
        QueryWrapper<CodeType> qw = new QueryWrapper<>();
        qw.eq(StrUtil.isNotBlank(codeId), "code_id", codeId);
        deviceMapper.deleteById(id);
        return Result.of(deviceTypeMapper.delete(qw));
    }

    @ApiOperation(value = "设备基本信息配置_设备类型新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Code", value = "code对象", required = true)
    })
    @PostMapping("device/addDevice")
    public Result addDevice(  String DeviceName){
        CodeTypeVo vo = new CodeTypeVo();
        vo.setCodeName(DeviceName);
        //若存在相同name，不插入，并查询code_id
        Integer codeId = deviceTypeMapper.selectByName(DeviceName);
        deviceMapper.insetCodeType(vo);
        Code code = new Code();
        code.setCodeDetail(DeviceName);
        code.setPid(8987);
        code.setCodeId(vo.getCodeId()==null?codeId:vo.getCodeId());
        return Result.of(deviceMapper.insert(code));
    }

    @ApiOperation(value = "设备型号管理_设备类型下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dropDownType", value = "下拉类型,1:设备类型 2:设备制造商 3:设备型号", required = true)
    })
    @PostMapping("devicemodel/typeList")
    public Result typeList( String dropDownType){
        return Result.of(deviceMapper.SelectdropDownType(dropDownType));
    }

//    @ApiOperation(value = "设备型号管理_新增系统分类")
//    @ApiImplicitParams({
//    })
//    @PostMapping("devicemodel/addSystemCode")
//    public Result deviceInfo( ){
//        return Result.of(deviceMapper.insetDeviceCompany(Company));
//    }
    @ApiOperation(value = "设备型号管理_新增/修改 设备")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/editDevice")
    public Result editDevice( @RequestBody DeviceModelVo[] arr ){
//        System.out.println(deviceModelVos);
//        List<DeviceModelVo> deviceModelVos = Arrays.asList(arr);
        for (DeviceModelVo vo :arr
             ) {
//            deviceMapper.updateDeviceModel(vo);
            CodeType codeType = new CodeType();
            codeType.setCodeName(vo.getCodeName());
            codeType.setCodeId(Integer.valueOf(vo.getModelCode()));
            //更改code_type表的codename
            deviceMapper.updateCodeType(codeType);
        }
        return Result.of(Result.ok());
    }


    @ApiOperation(value = "设备型号管理_新增/修改 设备")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/addDevice")
    public Result addDevice( @RequestBody DeviceModelVo[] arr ){
//        System.out.println(deviceModelVos);
//        List<DeviceModelVo> deviceModelVos = Arrays.asList(arr);
        for (DeviceModelVo vo :arr
        ) {
            deviceMapper.insertDeviceModel(vo);

        }
        return Result.of(Result.ok());
    }

    @ApiOperation(value = "设备型号管理_查询设备信息/编辑列表信息")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/getDeviceInfo")
    public Result getDeviceInfo( @RequestBody DeviceModelVo deviceModelVo ){
        List<DeviceModelVo> deviceInfo = deviceMapper.getDeviceInfo(deviceModelVo);
        //返回结果根据Category分类
        Map<String, List<DeviceModelVo>> map = deviceInfo.stream().collect(
                Collectors.groupingBy(
                        model -> model.getCategoryName()
                ));
        return Result.of(map);
    }


    @ApiOperation(value = "设备型号管理_设备型号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "deviceType", value = "设备类型", required = false),
            @ApiImplicitParam(name = "modelName", value = "设备型号名字", required = false),
            @ApiImplicitParam(name = "deviceCompany", value = "设备所属公司", required = false)
    })
    @PostMapping("devicemodel/modelList")
    public Result modelList(@ApiIgnore Page<ReportInterface> page, String deviceType, String modelName, String deviceCompany){
        Page<DeviceModel> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<DeviceModel> deviceModels = deviceMapper.selectDeviceModelList(deviceType, modelName, deviceCompany);
        jpage.setRecords(deviceModels);
//        jpage.setTotal(deviceMapper.selectDeviceListCount());
        return Result.of(jpage);
    }


    @ApiOperation(value = "设备型号管理_新建-增加制造商")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/addCompany")
    public Result addCompany( @RequestBody DeviceModel deviceModel ){
        int flag1=0;
        int flag2=0;
        deviceModel.setDeviceType(String.valueOf(8846));
        if (deviceModel.getDeviceType()==null){
            return Result.error(ResultCode.NOT_FOUND);
        }else {
            CodeType codeType = new CodeType();
            codeType.setCodeName(deviceModel.getDeviceType());
//            codeType = deviceMapper.selectIdByName(codeType);
//            codeType.setCodeId(288);
            if (deviceModel.getModelName() != null){
                //判断是否存在已有数据
                Code code = new Code();
                //此处8825为型号的id，先写死
                code.setPid(Integer.valueOf(deviceModel.getDeviceType()));
                code.setCodeId(49);
                code.setCodeDetail(deviceModel.getModelName());
                 flag1 = deviceMapper.insertCode(code);
                deviceModel.setModelCode(String.valueOf(code.getId()));

            }
            if ( deviceModel.getDeviceCompanyName() != null) {
                //判断是否存在已有数据
                Code code = new Code();
                code.setPid(Integer.valueOf(deviceModel.getDeviceType()));
                code.setCodeId(48);
                code.setCodeDetail(deviceModel.getDeviceCompanyName());
                 flag2 = deviceMapper.insertCode(code);
                deviceModel.setDeviceCompany(String.valueOf(code.getId()));
            }
            //判断型号是否已有
            if(flag1==0){
                return Result.error(ResultCode.VALIDATE_FAILED);
            }else {

                return Result.ok(deviceModel);
            }
        }

    }

    @ApiOperation(value = "设备型号管理_新建-查询设备基本信息(表格框架)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceType", value = "设备类型", required = false),
            @ApiImplicitParam(name = "modelCode", value = "设备型号", required = false),
            @ApiImplicitParam(name = "deviceCompany", value = "设备所属公司", required = false)
    })
    @PostMapping("devicemodel/getDeviceBasicInfo")
    public Result getDeviceBasicInfo(String deviceType,String deviceCompany,String modelCode){
        List<DeviceModelVo> deviceInfo = deviceMapper.getDeviceBasicInfo( deviceType, deviceCompany, modelCode);
        //返回结果根据Category分类
        Map<String, List<DeviceModelVo>> map = deviceInfo.stream().collect(
                Collectors.groupingBy(
                        model -> model.getCategoryName()
                ));
        return Result.of(map);
    }

    @ApiOperation(value = "设备型号管理_新建-新增系统分类")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/addSysCategory")
    public Result addSysCategory(String  categoryName ){
        if (categoryName==null||categoryName.equals("")){
            return Result.error(ResultCode.NOT_FOUND);
        }
        CodeTypeVo codeType = new CodeTypeVo();
        codeType.setCodeName(categoryName);
        int i = deviceMapper.insetCodeType(codeType);
        if (i==0){
            return Result.error(ResultCode.VALIDATE_FAILED);
        }
        Code code = new Code();
        code.setCodeId(codeType.getCodeId());
        code.setPid(244);
        deviceMapper.insert(code);
        return Result.of(Result.ok());
    }
    @ApiOperation(value = "设备型号管理_新建-新增参数")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/addParam")
    public Result addParam(@RequestBody DeviceModelVo[] deviceModelVos ){

        for (DeviceModelVo vo :deviceModelVos){
            CodeTypeVo codeType = new CodeTypeVo();
            Code code = new Code();
            //新增分类数据到codetype表
            if (vo.getCodeName()!=null&&vo.getCategory()!=null){
                codeType.setCodeName(vo.getCodeName());
                int i = deviceMapper.insetCodeType(codeType);
                String s = vo.getCode() == null ? String.valueOf(codeType.getCodeId()) : vo.getCode();
                vo.setCode(s);
                vo.setCodeValue(vo.getCodeValue());
                if (vo.getId()!=null&&!"".equals(vo.getId())){
                    deviceMapper.updateDeviceModelShow(vo);
                }else {
                    deviceMapper.insertDeviceModel(vo);
                }

                if (i!=0){
                    //插入后返回COdeid
                    //插入数据到code表
                    code.setCodeId(codeType.getCodeId());
                    code.setPid(Integer.valueOf(vo.getCategory()));
                    deviceMapper.insert(code);

                }

            }else {
                return Result.error(ResultCode.VALIDATE_FAILED);
            }

        }
        return Result.of(Result.ok());
    }

    @ApiOperation(value = "设备型号管理_删除设备")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/delDevice")
    public Result delDevice( String deviceType, String modelCode, String deviceCompany){
        //删除model_device表数据
        deviceMapper.delModelDevice(deviceType,modelCode,deviceCompany);
        deviceMapper.deleteById(modelCode);
        deviceMapper.deleteById(deviceCompany);
        //删除code表关联信息
        return Result.of(Result.ok());
    }


    @GetMapping("tree/list")
    @ApiOperation("设备树-列表")
    public Result listTree( ) {
        List<PropertyVo> list = treeMapper.selectList();
        //返回结果根据Category分类
//        Map<String, Map<String, List<PropertyVo>>> collect = list.stream().collect(
//                Collectors.groupingBy(
//                        PropertyVo::getDqName,
//
//                        Collectors.groupingBy(PropertyVo::getCzName)
//                ));
//        Map<String, Map<String, Map<String, List<PropertyVo>>>> collect = list.stream().collect(
//                Collectors.groupingBy(PropertyVo::getDqName,
//                        Collectors.groupingBy(
//                                PropertyVo::getCzName,
//
//                                Collectors.groupingBy(PropertyVo::getFjName)
//                        )));

        return Result.of(list);
    }

    @GetMapping("tree/get")
    @ApiOperation("设备树-获得节点信息")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    public Result getTree(@RequestParam("id") Long id) {
        List<PropertyVo>list = treeMapper.selectById(id);
        return Result.of(list);
    }

    @PostMapping ("tree/del")
    @ApiOperation("设备树-删除节点信息")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    public Result delTree( Integer id) {

        return Result.of( deviceMapper.deleteById(id));
    }

    @GetMapping("tree/getTypelist")
    @ApiOperation("设备树-获得下拉框信息")
    public Result getTypelist() {
            //查询分组节点
        List<CodeType> list = deviceMapper.selectDeviceListForTree();
        return Result.of(list);
    }

    @PostMapping("tree/add")
    @ApiOperation("设备树-新增")
    public Result addTree(@RequestBody  Code code) {
//        code.setPid(247);
//        code.setCodeId(246);
        code.setDataSources(4);
        return Result.of(deviceMapper.insert(code));
    }

    @PostMapping("tree/edit")
    @ApiOperation("设备树-编辑")
    public Result editTree(@RequestBody  PropertyVo vo) {

        //要是name和code_deatil的值相同，说明不是Property表的数据

     if(vo.getProName()!=null&&vo.getProName()!=""){
         Property property = new Property();
         property.setProName(vo.getName());
         property.setId(Integer.valueOf(vo.getCodeDetail()));
         return Result.of(treeMapper.updateById(property));

     }else {
         Code code = new Code();
         BeanUtils.copyProperties(vo,code);
         code.setDataSources(4);
         code.setCodeDetail(vo.getName());
         return Result.of(deviceMapper.updateById(code));

     }
    }

    @GetMapping("tree/getInfo")
    @ApiOperation("设备树-查看设备信息")
    public Result getTreeInfo(  String id,String codeId) {
        if (codeId == null || "".equals(codeId)) {
            return Result.error(ResultCode.NOT_FOUND);

        }
        //根据codeId查看同类型的设备
        List<Code> list = deviceMapper.selectDeviceList(null, codeId);
        List<CodeTypeVo> codeTypeVoList = null;
        if (list==null||list.size()==0){
            return   Result.error(500,"未查询到相关节点信息");
        }else {
            codeTypeVoList = deviceMapper.selectdeviceParam(String.valueOf(list.get(0).getId()));
        }
        List<CodeTypeVo> codeTypeVoList2 = deviceMapper.selectdeviceParam(id);
        if (codeTypeVoList==null){
          return   Result.error(500,"未查询到相关节点信息");
        }
        codeTypeVoList.forEach(vo1->{
            vo1.setId(null);
            codeTypeVoList2.forEach(vo2->{
                if (vo2.getCodeId().equals(vo1.getCodeId())) {
                    vo1.setCodeDetail(vo2.getCodeDetail());
                    vo1.setId(vo2.getId());
                }
                });
    });
        return  Result.of(codeTypeVoList);
    }

    @PostMapping("tree/editInfo")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "模板id", required = true),
            @ApiImplicitParam(name = "codeTypeListVo", value = "接口映射参数ID数组", required = true),
    })
    @ApiOperation("设备树-修改设备信息")
    public Result editTreeInfo( @RequestBody codeTypeListVo codeTypeListVo ) {

        System.out.println(codeTypeListVo);
        for (CodeTypeVo vo :codeTypeListVo.getIdsList()
        ) {


            Code code = new Code();
//            BeanUtils.copyProperties(vo,code);
            //此处的id为具体设备的id
            code.setPid(codeTypeListVo.getId());

            code.setCodeId(vo.getCodeId());
            code.setCodeDetail(vo.getCodeDetail());
            code.setDataSources(vo.getDataSources());
//            code.setCodeId(Integer.valueOf(vo.getModelCode()));
            //id为空说明是新增的数据
            if (vo.getId()==null||vo.getId().equals("")){
                deviceMapper.insertCode(code);
            }{
                code.setId(vo.getId());
                deviceMapper.updateById(code);
            }


        }
        return Result.of(Result.ok());
    }

    @ApiOperation(value = "设备型号管理_根据型号查询id")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/selModelCode")
    public Result editTreeInfo( String codeDetail ) {
        Code s = deviceMapper.selModelCode(codeDetail);
        if (s==null){
            return Result.error(500,"未查询到相关型号信息");
        }else {
            return Result.of(s);
        }
    }




    }
