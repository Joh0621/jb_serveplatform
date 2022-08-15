package com.bonc.jibei.mapper;

import com.bonc.jibei.entity.Qualified;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 数据质量条数表
 * @author wangtao
 * @date 2022-08-08
 */
@Mapper
@Repository
public interface QualifiedMapper {

    /**
     * 新增
     * @author wangtao
     * @date 2022/08/08
     **/
    int insert(Qualified qualified);

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
    int update(Qualified qualified);

    /**
     * 查询 根据主键 id 查询
     * @author wangtao
     * @date 2022/08/08
     **/
    Qualified load(int id);

    /**
     * 查询 分页查询
     * @author wangtao
     * @date 2022/08/08
     **/
    List<Qualified> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author wangtao
     * @date 2022/08/08
     **/
    int pageListCount(int offset,int pagesize);

}
