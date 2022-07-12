package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.Code;
import com.bonc.jibei.entity.CodeType;
import com.bonc.jibei.entity.DeviceModel;
import com.bonc.jibei.entity.Property;
import com.bonc.jibei.vo.CodeTypeVo;
import com.bonc.jibei.vo.DeviceModelVo;
import com.bonc.jibei.vo.PropertyVo;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/5 16:46
 * @Description:  接口参数
 */
public interface TreeMapper extends  RootMapper<Property>{
     List<PropertyVo> selectList();

     /**
      * 通过返回的pro_name是否为空判断是否来自pro表
      * @param id
      * @return
      */
     List<PropertyVo>   selectById(Long id);

   List<Property>  stationdownList(String id);

   void delCzInfo(String id);
}
