package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.PassRateStatistics;
import com.bonc.jibei.entity.Qualified;
import com.bonc.jibei.entity.Station;
import com.bonc.jibei.vo.DataQualityErrorVo;
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
                                                @Param("type") String type,
                                                @Param("stationId") String stationId  );

    String passRateStatisticsSortJb(@Param("startTime") String startTime,
                                    @Param("endTime") String endTime,
                                    @Param("type") String type,
                                    @Param("stationId") String stationId  );


    String passRateStatisticsSortPm(@Param("startTime") String startTime,
                                    @Param("endTime") String endTime,
                                    @Param("type") String type,
                                    @Param("stationId") String stationId  );
    List<Qualified> SelPassRateTrend(@Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     @Param("type") String type,
                                     @Param("stationId") String stationId,
                                     @Param("dataFlag") String dataFlag);


    List<DataQualityErrorVo> selErrorDistributedForData(@Param("startTime") String startTime,
                                                        @Param("endTime") String endTime,
                                                        @Param("type") String type,
                                                        @Param("stationId") String stationId)
            ;
    List<DataQualityErrorVo> selErrorDistributedForError(@Param("startTime") String startTime,
                                                         @Param("endTime") String endTime,
                                                         @Param("type") String type,
                                                         @Param("stationId") String stationId);
    List<DataQualityError> selErrorRecord(IPage<?> page,
                                          @Param("dataSource") String dataSource,
                                          @Param("errorType") String errorType,
                                          @Param("stationId") String stationId,
                                          @Param("DeviceId") String DeviceId,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime);

    DataQualityError selErrorRecordTotal(@Param("dataSource") String dataSource,
                                         @Param("errorType") String errorType,
                                         @Param("stationId") String stationId,
                                         @Param("DeviceId") String DeviceId,
                                         @Param("startTime") String startTime,
                                         @Param("endTime") String endTime);


    List<Station> selStation(
            @Param("type") String type,
            @Param("aname") String aname);

    List<String> selDevice( @Param("stationId") String stationId,
                            @Param("dataSource") String dataSource,
                            @Param("errorType") String errorType,
                            @Param("type") String type);

    DataQualityError  errorDataStatisticsTablieName (@Param("id") String id);

}
