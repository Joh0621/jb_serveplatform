package com.bonc.jibei.mapper;

import com.bonc.jibei.entity.StationModelRel;
import com.bonc.jibei.vo.StationModelRelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * @Author: dupengling
 * @DateTime: 2022/4/28 19:13
 * @Description: 模板相关的场站
 */
@Mapper
public interface StationModelRelMapper extends  RootMapper<StationModelRel>{
    List<StationModelRelVo> selectStationModelRelVoList(@Param("modelId") Integer modelId);
}
