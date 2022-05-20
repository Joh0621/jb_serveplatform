package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.ReportParamsMap;
import com.bonc.jibei.vo.ModelInterParamMapVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 23:50
 * @Description: 模板接口参数映射
 */
@Mapper
public interface ReportParamsMapMapper  extends  RootMapper<ReportParamsMap>{

    /**
     *  接口参数映射列表
     * @param page：页码
     * @param interCode:接口编码
     * @param paramCode:参数编码
     * @param paramName:参数名
     * @return 返回映射列表
     */
    List<ModelInterParamMapVo> selectReportParamsMapList(IPage<?> page, @Param("interCode") String interCode, @Param("paramCode") String paramCode, @Param("paramName") String paramName);
    Integer selectCount(@Param("interCode") String interCode, @Param("paramCode") String paramCode, @Param("paramName") String paramName);
}
