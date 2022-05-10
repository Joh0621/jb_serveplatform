package com.bonc.jibei.service;

import com.alibaba.fastjson.JSONObject;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/4/29 11:06
 */
public interface ReportService {

    String generate(JSONObject params) throws IOException, TemplateException;

}
