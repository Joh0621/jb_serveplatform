package com.bonc.jibei.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonc.jibei.entity.ReportMng;
import com.bonc.jibei.mapper.ReportMngMapper;
import com.bonc.jibei.service.ReportMngService;
import com.bonc.jibei.vo.ReportMngList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 16:17
 * @Description: TODO
 */
@Service
public class ReportMngServiceImpl extends ServiceImpl<ReportMngMapper,ReportMng> implements ReportMngService {
    @Resource
    private ReportMngMapper reportMngMapper;

    @Override
    public List<ReportMngList> reportMngList(IPage<ReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus, Long start, Long size) {

        return reportMngMapper.selectReportMngList(page,stationName,year,quarter,stationType,reportStatus, start, size);
    }

    @Override
    public List<String> urlList(List<Integer> ids) {
        return reportMngMapper.selectDocUrl(ids);
    }
}
