package com.bonc.jibei.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonc.jibei.entity.JbReportMng;
import com.bonc.jibei.mapper.JbReportMngMapper;
import com.bonc.jibei.service.JbReportMngService;
import com.bonc.jibei.vo.JbReportMngList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/19 16:17
 * @Description: TODO
 */
@Service
public class JbReportMngServiceImpl extends ServiceImpl<JbReportMngMapper,JbReportMng> implements JbReportMngService {
    @Resource
    private JbReportMngMapper jbReportMngMapper;
    @Override
    public List<JbReportMngList> jbReportMngList(IPage<JbReportMngList> page, String stationName, Integer year, Integer quarter, Integer stationType, Integer reportStatus, Long start, Long size) {

        return jbReportMngMapper.selectJbReportMngList(page,stationName,year,quarter,stationType,reportStatus, start, size);
    }

    @Override
    public List<String> urlList(List<Integer> ids) {
        return jbReportMngMapper.selectDocUrl(ids);
    }

}
