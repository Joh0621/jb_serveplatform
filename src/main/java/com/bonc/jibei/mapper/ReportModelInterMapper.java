package com.bonc.jibei.mapper;

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
     * 当前时间的上一个季度的数据
     */
    List<ReportModelInter> selectReportModelInter(@Param("year") Integer year,@Param("quarter") Integer quarter);

    /**
     * 重新生成 报表数据
     */
    List<ReportModelInter> selectReReportModelInter(@Param("id") Integer id);
}
