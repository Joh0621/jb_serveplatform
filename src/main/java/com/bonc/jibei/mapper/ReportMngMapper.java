package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.vo.ReportMngIds;
import com.bonc.jibei.vo.ReportMngList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 13:55
 * @Description: TODO
 */
@Mapper
public interface ReportMngMapper extends RootMapper<ReportMng> {

    List<ReportMngList> selectReportMngList(IPage<?> page, @Param("stationName") String stationName, @Param("year") Integer year, @Param("quarter") Integer quarter, @Param("stationType") Integer stationType, @Param("reportStatus") Integer reportStatus);

    Integer selectCount(@Param("stationName") String stationName, @Param("year") Integer year, @Param("quarter") Integer quarter, @Param("stationType") Integer stationType, @Param("reportStatus") Integer reportStatus);

    List<String> selectDocUrl(@Param("ids") List<Integer> ids);

    /**
     * @description: 批量复核
     */
    @Update("<script>" +
            "<foreach collection='idsList' item='idsList'   separator=';'> " +
            " update jb_report_mng set report_status=1 where id= #{idsList.id}" +
            "</foreach> " +
            "</script>")
    int updateReportStatusBatch(@Param("idsList") List<ReportMngIds> idsList);
}
