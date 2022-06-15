package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.Code;
import com.bonc.jibei.entity.InterParams;
import com.bonc.jibei.vo.CodeTypeVo;
import com.bonc.jibei.vo.ModelInterParamMapVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/5 16:46
 * @Description:  接口参数
 */
public interface DeviceMapper extends  RootMapper<Code>{
    List<Code> selectDeviceList(IPage<?> page);
    int selectDeviceListCount();

    List<CodeTypeVo>  selectdeviceParam(String pId);
}