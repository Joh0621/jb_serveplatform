package com.bonc.jibei.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.api.ResultCode;
import com.bonc.jibei.entity.*;
import com.bonc.jibei.mapper.DeviceTypeMapper;
import com.bonc.jibei.vo.CodeTypeVo;
import com.bonc.jibei.mapper.DeviceMapper;
import com.bonc.jibei.vo.DeviceModelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Api(tags = "台账配置接口")
@RestController
public class DeviceController {
    @Resource
    DeviceMapper deviceMapper;

    @Resource
    DeviceTypeMapper deviceTypeMapper;

    @ApiOperation(value = "设备基本信息配置_设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
    })
    @GetMapping("device/deviceInfoList")
    public Result deviceList(@ApiIgnore Page<Code> page){
        Page<Code> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<Code> list = deviceMapper.selectDeviceList(jpage);
        jpage.setRecords(list);
        jpage.setTotal(deviceMapper.selectDeviceListCount());
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
        if (vo == null ) {
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
            deviceMapper.insetCodeType(vo);
            System.out.println();
            Code code = new Code();

            BeanUtils.copyProperties(vo,code);
            code.setPid(8830);
            System.out.println(code);
            return Result.of(deviceMapper.insert(code));
        }
//        return Result.of(null);
    }

    @ApiOperation(value = "设备基本信息配置_参数新增")
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
        System.out.println(DeviceName);
        Code code = new Code();
        code.setCodeDetail(DeviceName);
        code.setPid(247);
        code.setCodeId(246);
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

//    @ApiOperation(value = "设备型号管理_新增设备制造商")
//    @ApiImplicitParams({
//    })
//    @PostMapping("devicemodel/addCompany")
//    public Result deviceInfo(String Company ){
//        return Result.of(deviceMapper.insetDeviceCompany(Company));
//    }
    @ApiOperation(value = "设备型号管理_新增设备")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/addDevice")
    public Result deviceInfo(String Company ){

        return Result.of(deviceMapper.insetDeviceCompany(Company));
    }

    @ApiOperation(value = "设备型号管理_查询设备信息")
    @ApiImplicitParams({
    })
    @PostMapping("devicemodel/getDeviceInfo")
    public Result getDeviceInfo(  ){
        List<DeviceModel> deviceInfo = deviceMapper.getDeviceInfo();
        DeviceModelVo vo = new DeviceModelVo();

        HashMap<String, Object> map = new HashMap<>();
        ArrayList<DeviceModel> codeTypes = new ArrayList<>();
        for (DeviceModel model:deviceInfo
             ) {
            codeTypes.add(model);
            map.put(model.getCategory(),codeTypes);
        }
        return Result.of(map);
//        return Result.of(deviceMapper.insetDeviceCompany(Company));
    }


    @ApiOperation(value = "设备型号管理_设备型号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页，默认值为 1", required = true),
            @ApiImplicitParam(name = "size", value = "页大小，默认值为 10", required = true),
            @ApiImplicitParam(name = "deviceType", value = "设备类型", required = false),
            @ApiImplicitParam(name = "deviceModel", value = "设备型号", required = false),
            @ApiImplicitParam(name = "deviceCompany", value = "设备所属公司", required = false)
    })
    @PostMapping("devicemodel/modelList")
    public Result modelList(@ApiIgnore Page<ReportInterface> page, String deviceType, String deviceModel, String deviceCompany){
        Page<DeviceModel> jpage = new Page<>(page.getCurrent(), page.getSize());
        jpage.setSearchCount(false);
        List<DeviceModel> deviceModels = deviceMapper.selectDeviceModelList(deviceType, deviceModel, deviceCompany);
        jpage.setRecords(deviceModels);
//        jpage.setTotal(deviceMapper.selectDeviceListCount());
        return Result.of(jpage);
    }

}
