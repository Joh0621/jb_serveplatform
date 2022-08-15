package com.bonc.jibei.service;

import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.PassRateStatistics;
import com.bonc.jibei.entity.Qualified;

import java.util.List;
import java.util.Map;

/**
 * @description data_quality_error
 * @author wangtao
 * @date 2022-08-08
 */
public interface DataQualityErrorService {

    /**
     * 新增
     */
    public Object insert(DataQualityError dataQualityError);

    /**
     * 删除
     */
    public Object delete(int id);

    /**
     * 更新
     */
    public Object update(DataQualityError dataQualityError);

    /**
     * 根据主键 id 查询
     */
    public DataQualityError load(int id);

    /**
     * 分页查询
     */
//    public Map<String,Object> pageList(int offset, int pagesize);

    PassRateStatistics passRateStatistics(String startTime, String endTime, String type);


    List<Map<String,Object>>  selPassRateTrend(String startTime, String endTime, String type);



    List<DataQualityError>  selErrorRecord(String dataSource,String errorType, String stationId, String DeviceId);

}