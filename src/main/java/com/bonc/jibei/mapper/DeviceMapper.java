package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.Code;
import com.bonc.jibei.entity.CodeType;
import com.bonc.jibei.entity.DeviceModel;
import com.bonc.jibei.vo.CodeTypeVo;
import com.bonc.jibei.vo.DeviceModelVo;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/5 16:46
 * @Description:  接口参数
 */
public interface DeviceMapper extends  RootMapper<Code>{
    List<Code> selectDeviceList(IPage<?> page,String codeId);

    List<CodeType> selectDeviceListForTree();
    int selectDeviceListCount(String codeId);

    List<CodeTypeVo>  selectdeviceParam(String pid);

    int  insetCodeType(CodeTypeVo vo);

    int  insetDeviceCompany(String Company);
    int  updateCodeType(CodeType codeType);

   int insertDeviceModel(DeviceModelVo deviceModel);


    int updateDeviceModelShow(DeviceModelVo deviceModel);
    List<DeviceModel>  selectDeviceModelList(String deviceType, String modelName, String deviceCompany );

    /**
     * 查询设备型号下拉框
     *
     * @return 设备型号
     */
    List<Code> SelectdropDownType(String dropDownType );


//    DeviceModel  SelectDeviceInfoByType(String deviceType);

    /**
     * 查询设备详细信息
     * @return
     */
    List<DeviceModelVo> getDeviceInfo(DeviceModelVo  deviceModelVo);

    List<DeviceModelVo> getDeviceBasicInfo(  String deviceType,String deviceCompany,String modelCode);


    /**
     * 根据设备类型名查询对应的id
     */
    CodeType selectIdByName(CodeType CodeType);
   int  insertCode(Code code);


    /**
     * 删除某一设备数据
     */
   int  delModelDevice( String deviceType,String modelCode,String deviceCompany);


   Code selModelCode(String codeDetail);

}
