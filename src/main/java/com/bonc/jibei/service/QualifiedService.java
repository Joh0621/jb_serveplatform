package com.bonc.jibei.service;

import com.bonc.jibei.entity.Qualified;

import java.util.Map;

/**
 * @description 数据质量条数表
 * @author wangtao
 * @date 2022-08-08
 */
public interface QualifiedService {

    /**
     * 新增
     */
    public Object insert(Qualified qualified);

    /**
     * 删除
     */
    public Object delete(int id);

    /**
     * 更新
     */
    public Object update(Qualified qualified);

    /**
     * 根据主键 id 查询
     */
    public Qualified load(int id);

    /**
     * 分页查询
     */
    public Map<String,Object> pageList(int offset, int pagesize);

}
