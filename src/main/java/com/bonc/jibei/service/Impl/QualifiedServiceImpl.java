package com.bonc.jibei.service.Impl;

import com.bonc.jibei.api.Result;
import com.bonc.jibei.entity.Qualified;
import com.bonc.jibei.mapper.QualifiedMapper;
import com.bonc.jibei.service.QualifiedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 数据质量条数表
 * @author wangtao
 * @date 2022-08-08
 */
@Service
public class QualifiedServiceImpl implements QualifiedService {

    @Resource
    private QualifiedMapper qualifiedMapper;


    @Override
    public Object insert(Qualified qualified) {

        // valid
        if (qualified == null) {
            return Result.error(500,"必要参数缺失");
        }

        qualifiedMapper.insert(qualified);
        return Result.ok();
    }


    @Override
    public Object delete(int id) {
        int ret = qualifiedMapper.delete(id);
        return ret>0?Result.ok():Result.error(500,"操作失败");
    }


    @Override
    public Object update(Qualified qualified) {
        int ret = qualifiedMapper.update(qualified);
        return ret>0?Result.ok():Result.error(500,"操作失败");
    }


    @Override
    public Qualified load(int id) {
        return qualifiedMapper.load(id);
    }


    @Override
    public Map<String,Object> pageList(int offset, int pagesize) {

        List<Qualified> pageList = qualifiedMapper.pageList(offset, pagesize);
        int totalCount = qualifiedMapper.pageListCount(offset, pagesize);

        // result
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);

        return result;
    }

}
