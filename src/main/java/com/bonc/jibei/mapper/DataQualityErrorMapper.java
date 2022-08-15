package com.bonc.jibei.mapper;

import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.PassRateStatistics;
import com.bonc.jibei.entity.Qualified;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description data_quality_error
 * @author wangtao
 * @date 2022-08-08
 */
@Mapper
@Repository
public interface DataQualityErrorMapper {

    /**
     * 新增
     * @author wangtao
     * @date 2022/08/08
     **/
    int insert(DataQualityError dataQualityError);

    /**
     * 刪除
     * @author wangtao
     * @date 2022/08/08
     **/
    int delete(int id);

    /**
     * 更新
     * @author wangtao
     * @date 2022/08/08
     **/
    int update(DataQualityError dataQualityError);

    /**
     * 查询 根据主键 id 查询
     * @author wangtao
     * @date 2022/08/08
     **/
    DataQualityError load(int id);

    /**
     * 查询 分页查询
     * @author wangtao
     * @date 2022/08/08
     **/
    List<DataQualityError> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author wangtao
     * @date 2022/08/08
     **/
    int pageListCount(int offset,int pagesize);


    List<PassRateStatistics> passRateStatistics(@Param("startTime") String startTime,
                                          @Param("endTime") String endTime,
                                          @Param("type") String type);

    List<Qualified> SelPassRateTrend(@Param("startTime") String startTime,
                               @Param("endTime") String endTime,
                               @Param("type") String type);



    List<DataQualityError> SelErrorRecord(@Param("dataSource") String dataSource,
                                          @Param("errorType") String errorType,
                                          @Param("stationId") String stationId,
                                          @Param("DeviceId") String DeviceId);


}
