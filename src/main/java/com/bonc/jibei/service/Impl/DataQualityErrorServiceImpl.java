package com.bonc.jibei.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.PassRateStatistics;
import com.bonc.jibei.entity.Qualified;
import com.bonc.jibei.mapper.DataQualityErrorMapper;
import com.bonc.jibei.service.DataQualityErrorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description data_quality_error
 * @author wangtao
 * @date 2022-08-08
 */
@Service
public class DataQualityErrorServiceImpl implements DataQualityErrorService {

    @Resource
    private DataQualityErrorMapper dataQualityErrorMapper;


    @Override
    public Object insert(DataQualityError dataQualityError) {

        // valid
        if (dataQualityError == null) {
            return Result.error(500,"必要参数缺失");
        }

        dataQualityErrorMapper.insert(dataQualityError);
        return Result.ok();
    }


    @Override
    public Object delete(int id) {
        int ret = dataQualityErrorMapper.delete(id);
        return ret>0?Result.ok():Result.error(500,"操作失败");
    }


    @Override
    public Object update(DataQualityError dataQualityError) {
        int ret = dataQualityErrorMapper.update(dataQualityError);
        return ret>0?Result.ok():Result.error(500,"操作失败");
    }


    @Override
    public DataQualityError load(int id) {
        return dataQualityErrorMapper.load(id);
    }

    @Override
    public PassRateStatistics passRateStatistics(String startTime, String endTime, String type) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<PassRateStatistics> passRateStatisticsList = dataQualityErrorMapper.passRateStatistics(startTime, endTime, type);
        PassRateStatistics PassRateStatistics = new PassRateStatistics();
        Integer total = 0;
        Integer QualifiedNum = 0;
        for (PassRateStatistics ps:passRateStatisticsList){
          //传入参数为全部时
            if (type==null||type.equals(0)){
                total+=ps.getTotalNum();
                QualifiedNum+=ps.getQualifiedNum();
                PassRateStatistics.setTotalNum(total);
                PassRateStatistics.setQualifiedNum(QualifiedNum);
                PassRateStatistics.setQualifiedRate( Double.valueOf( NumberUtil.round((Double.valueOf(QualifiedNum)/total*100), 2).toString()));
               if (ps.getTypeId().equals(1)){
                   PassRateStatistics.setWindQualifiedRate(ps.getQualifiedRate());
               }else if (ps.getTypeId().equals(2)){
                   PassRateStatistics.setPVQualifiedRate(ps.getQualifiedRate());
               }
            }else {
                BeanUtil.copyProperties(ps,PassRateStatistics);
            }
        }
        return PassRateStatistics;
    }

    @Override
    public List<Map<String, Object>> selPassRateTrend(String startTime, String endTime, String type) {
        List<Qualified> qualifieds = dataQualityErrorMapper.SelPassRateTrend(startTime, endTime, type);
//        Map<Object, List<Qualified>> map = qualifieds.stream().collect(
//                Collectors.groupingBy(
//                        qualified -> qualified.getDateTime()
//                ));
        List<Map<String,Object>> windList = new ArrayList<>();
        List<Map<String,Object>> powerList = new ArrayList<>();

        for (Qualified qualified:qualifieds){
            if ("1".equals(qualified.getDataSource())){
//                windList.add(qualified);
            }
        }
        return null;
    }

    @Override
    public List<DataQualityError> selErrorRecord(String dataSource, String errorType, String stationId, String DeviceId) {
        return dataQualityErrorMapper.SelErrorRecord(dataSource, errorType, stationId, DeviceId);
    }


//    @Override
//    public Map<String,Object> pageList(int offset, int pagesize) {
//
//        List<DataQualityError> pageList = (List<DataQualityError>) dataQualityErrorMapper.pageList(offset, pagesize);
//        int totalCount = dataQualityErrorMapper.pageListCount(offset, pagesize);
//
//        // result
//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("pageList", pageList);
//        result.put("totalCount", totalCount);
//
//        return result;
//    }

}
