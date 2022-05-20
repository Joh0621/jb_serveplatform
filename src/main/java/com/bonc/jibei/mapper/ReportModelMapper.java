package com.bonc.jibei.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.ReportModel;
import com.bonc.jibei.vo.ModelInterParamMapVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/28 19:22
 * @Description: TODO
 */
@Mapper
public interface ReportModelMapper  extends  RootMapper<ReportModel>{
    /*
     *  模板报告列表
     * @param page：页码
     * @param modelType:模板类型
     * @param reportStatus:报告状态
     * @param name:模板名或 报告名
     * @return 返回映射列表
     */
    List<ReportModel> selectModelReportList(IPage<?> page, @Param("modelType") Integer modelType, @Param("reportStatus") Integer reportStatus, @Param("name") String name);
    Integer reportSelectCount(@Param("modelType") Integer modelType, @Param("reportStatus") Integer reportStatus, @Param("name") String name);
}
