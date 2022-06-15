package com.bonc.jibei.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.api.ResultCode;
import com.bonc.jibei.entity.Code;
import com.bonc.jibei.vo.CodeTypeVo;
import com.bonc.jibei.mapper.DeviceMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "台账配置接口")
@RestController
public class DeviceController {
    @Resource
    DeviceMapper deviceMapper;



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

//    @ApiOperation(value = "设备基本信息配置_参数新增")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "设别类型id", required = true),
//    })
//    @GetMapping("device/adddeviceParam")
//    public Result deviceParam(@RequestBody CodeTypeVo vo){
//        if (vo == null ) {
//            return Result.error(ResultCode.NOT_FOUND);
//        }
//        List<CodeTypeVo> list = deviceMapper.selectdeviceParam(id);
//        return Result.of(list);
//    }
}
