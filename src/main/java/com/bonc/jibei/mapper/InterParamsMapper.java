package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.InterParams;
import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.vo.ModelInterParamMapVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/5 16:46
 * @Description:  接口参数
 */
public interface InterParamsMapper  extends  RootMapper<InterParams>{
    List<ModelInterParamMapVo> selectInterParamList(IPage<?> page, @Param("interId") Integer interId, @Param("interName") String interName, @Param("paramName") String paramName);
    Integer selectCount(@Param("interId") Integer interId, @Param("interName") String interName, @Param("paramName") String paramName);
}
