package com.bonc.jibei.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.ReportCfg;
import com.bonc.jibei.entity.ReportModel;
import com.bonc.jibei.vo.ModelInterParamMapVo;
import com.bonc.jibei.vo.ModelInterfaceRelListVo;
import com.bonc.jibei.vo.ReportModelVo;
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
    /*selectReportList
     *  模板报告列表
     * @param page：页码
     * @param modelType:模板类型
     * @param reportStatus:报告状态
     * @param name:模板名或 报告名
     * @return 返回映射列表
     */
    List<ReportModel> selectModelReportList(IPage<?> page, @Param("modelType") Integer modelType, @Param("modelStatus") Integer modelStatus, @Param("name") String name);
    Integer reportSelectCount(@Param("modelType") Integer modelType, @Param("modelStatus") Integer modelStatus, @Param("name") String name);
    List<ReportModel> selectReportList ();

    List<ModelInterfaceRelListVo> selectInfoByModelId(@Param("id") Integer id);


    List<ReportModelVo> selectReportCfgList(IPage<?> page, @Param("modelType") Integer modelType, @Param("modelStatus") Integer modelStatus, @Param("name") String name);


    Integer reportCfgSelectCount(@Param("modelType") Integer modelType, @Param("modelStatus") Integer modelStatus, @Param("name") String name);


    List<ReportModelVo> selectReportCfgListData( @Param("modelName") Integer modelName, @Param("modelVersion") Integer modelVersion, @Param("reportType") String reportType, @Param("reportName") String reportName);



}
