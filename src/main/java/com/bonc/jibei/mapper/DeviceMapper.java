package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.Code;
import com.bonc.jibei.entity.CodeType;
import com.bonc.jibei.entity.DeviceModel;
import com.bonc.jibei.vo.CodeTypeVo;

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

    int  insetCodeType(CodeTypeVo vo);

    int  insetDeviceCompany(String Company);
    int  updateCodeType(CodeType codeType);

    List<DeviceModel>  selectDeviceModelList(String deviceType, String deviceModel, String deviceCompany );

    /**
     * 查询设备型号下拉框
     *
     * @return 设备型号
     */
    List<Code> SelectdropDownType(String dropDownType );


//    DeviceModel  SelectDeviceInfoByType(String deviceType);


    List<DeviceModel> getDeviceInfo();
}
