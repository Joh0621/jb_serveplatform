package com.bonc.jibei.controller;

import com.bonc.jibei.api.Result;
import com.bonc.jibei.entity.Qualified;
import com.bonc.jibei.service.QualifiedService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description 数据质量条数表
 * @author wangtao
 * @date 2022-08-08
 */
@RestController
@RequestMapping(value = "/qualified")
public class QualifiedController {

    @Resource
    private QualifiedService qualifiedService;

    /**
     * 新增
     * @author wangtao
     * @date 2022/08/08
     **/
    @RequestMapping("/insert")
    public Object insert(Qualified qualified){
        return qualifiedService.insert(qualified);
    }

    /**
     * 刪除
     * @author wangtao
     * @date 2022/08/08
     **/
    @RequestMapping("/delete")
    public Result delete(int id){
        return Result.of(qualifiedService.delete(id));
    }

    /**
     * 更新
     * @author wangtao
     * @date 2022/08/08
     **/
    @RequestMapping("/update")
    public Result update(Qualified qualified){
        return Result.of(qualifiedService.update(qualified));
    }

    /**
     * 查询 根据主键 id 查询
     * @author wangtao
     * @date 2022/08/08
     **/
    @RequestMapping("/load")
    public Object load(int id){
        return qualifiedService.load(id);
    }

    /**
     * 查询 分页查询
     * @author wangtao
     * @date 2022/08/08
     **/
    @RequestMapping("/pageList")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
                                        @RequestParam(required = false, defaultValue = "10") int pagesize) {
        return qualifiedService.pageList(offset, pagesize);
    }

}