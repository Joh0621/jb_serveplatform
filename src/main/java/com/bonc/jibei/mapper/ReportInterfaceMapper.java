package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.vo.ReportMngList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/4/29 11:32
 */
public interface ReportInterfaceMapper extends RootMapper<ReportInterface> {
    List<ReportInterface> selectReportInterList(IPage<?> page, @Param("name") String name,@Param("type") String type,@Param("modelId") Integer modelId);
    Integer selectCount(@Param("name") String name, @Param("type") String type,@Param("modelId") Integer modelId);
}
