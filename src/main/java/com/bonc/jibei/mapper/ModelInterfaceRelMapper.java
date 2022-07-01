package com.bonc.jibei.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.entity.InterParams;
import com.bonc.jibei.entity.ModelInterfaceRel;
import com.bonc.jibei.vo.ModelInterfaceRelListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/18 23:42
 * @Description: TODO
 */
@Mapper
public interface ModelInterfaceRelMapper extends  RootMapper<ModelInterfaceRel>{
    /**
     *  接口参数列表
     * @param page：页码
     * @param interCode:接口编码
     * @param paramCode:参数编码
     * @param interDesc:接口描述
     * @return 返回映射列表
     */
    List<ModelInterfaceRelListVo> selectInterParamRelList(IPage<?> page, @Param("interCode") String interCode, @Param("paramCode") String paramCode, @Param("interDesc") String interDesc);

    /**
     * sql未使用
     * @param interCode
     * @param paramCode
     * @param interDesc
     * @return
     */
    Integer selectCount(@Param("interCode") String interCode, @Param("paramCode") String paramCode, @Param("interDesc") String interDesc);
}
