package com.bonc.jibei.mapper;

import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.vo.ReportModelInter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 22:05
 * @Description: TODO
 */
@Mapper
public interface ReportModelInterMapper extends  RootMapper<ReportModelInter>{
    /**
     * 新生成报告得模板
     */
    List<ReportMng> selectReportModel(@Param("year") Integer year,@Param("quarter") Integer quarter);
    /**
     * 重新生成报告得模板
     */
    List<ReportMng> selectReReportModel(@Param("reportStatus") Integer reportStatus);

    /**
     * 当前时间的上一个季度的数据
     *   * param:modelType 模板类型；modelId 模板id
     */
    List<ReportModelInter> selectReportModelInter(@Param("modelType") Integer modelType,@Param("modelId") Integer modelId);

    /**
     * 重新生成 报表数据
     * param:modelType 模板类型；modelId 模板id
     */
    List<ReportModelInter> selectReReportModelInter(@Param("modelType") Integer modelType,@Param("modelId") Integer modelId);

    /**
     * 获取模板接口
     * param:modelType 模板类型；modelId 模板id
     */
    List<ReportInterface> selectReportInter(@Param("modelId") Integer modelId);
}
