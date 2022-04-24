package com.bonc.jibei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bonc.jibei.entity.ReportAuthLog;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/20 16:25
 * @Description: TODO
 */
public interface ReportAuthLogService extends IService<ReportAuthLog> {
    int insertReportAuth(Integer id,String userName,String memo,String operName);
}
