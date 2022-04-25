package com.bonc.jibei.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bonc.jibei.entity.JbReportMng;
import com.bonc.jibei.vo.JbReportMngList;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 16:17
 * @Description: TODO
 */
public interface JbReportMngService extends IService<JbReportMng> {

    List<JbReportMngList> jbReportMngList(IPage<JbReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus, Long start, Long size);

    List<String> urlList(List<Integer> ids);
}
