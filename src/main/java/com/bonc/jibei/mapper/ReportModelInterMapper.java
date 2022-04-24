package com.bonc.jibei.mapper;

import com.bonc.jibei.vo.ReportModelInter;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 22:05
 * @Description: TODO
 */
public interface ReportModelInterMapper extends  RootMapper<ReportModelInter>{
    List<ReportModelInter> selectReportModelInter(@Param("reportStatus") Integer reportStatus);

}
