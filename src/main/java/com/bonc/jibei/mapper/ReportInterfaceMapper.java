package com.bonc.jibei.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.vo.ModelInterfaceRelListVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/4/29 11:32
 */
public interface ReportInterfaceMapper extends RootMapper<ReportInterface> {
    /**
     * 接口列表列表
     * @param page
     * @param interType
     * @return
     */
    List<ReportInterface> selectReportInterList(IPage<?> page, @Param("interType") String interType);
    Integer selectCount(@Param("interType") String interType);

    /**
     * 模板报告接口列表
     * @param page
     * @param modelName
     * @param interType
     * @param modelId
     * @return
     */
    List<ModelInterfaceRelListVo> selectReportModelInterList(IPage<?> page, @Param("modelName") String modelName, @Param("interType") String interType, @Param("modelId") Integer modelId);
    Integer selectModelInterCount(@Param("modelName") String modelName, @Param("interType") String interType,@Param("modelId") Integer modelId);
}
