package com.bonc.jibei.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bonc.jibei.entity.ReportAuthLog;
import com.bonc.jibei.mapper.ReportAuthLogMapper;
import com.bonc.jibei.service.ReportAuthLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/20 16:26
 * @Description: TODO
 */
@Service
public class ReportAuthLogServiceImpl extends ServiceImpl<ReportAuthLogMapper, ReportAuthLog> implements ReportAuthLogService {
    @Resource
    private ReportAuthLogMapper reportAuthLogMapper;

    @Override
    public int insertReportAuth(Integer id, String userName, String memo, String operName) {
        ReportAuthLog log=new ReportAuthLog();
        log.setReportId(id);
        log.setOperUser(userName);
        log.setOperTime(LocalDateTime.now());
        log.setOperName(operName);
        log.setMemo(memo);
        return reportAuthLogMapper.insert(log);
    }
}
