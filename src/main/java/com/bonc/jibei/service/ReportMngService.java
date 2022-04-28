package com.bonc.jibei.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.vo.ReportMngList;
import com.bonc.jibei.vo.ReportModelInter;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 16:17
 * @Description: TODO
 */
public interface ReportMngService extends IService<ReportMng> {
    List<ReportMngList> reportMngList(IPage<ReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus, Long start, Long size);
    List<String> urlList(List<Integer> ids);
    int insertReport(ReportModelInter reportlist);//新生成报告
    int updateReport(ReportMng reportMng);//重新生成报告
}
