package com.bonc.jibei.service.Impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bonc.jibei.api.ValueType;
import com.bonc.jibei.entity.ImgData;
import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.mapper.ReportInterfaceMapper;
import com.bonc.jibei.service.ReportService;
import com.bonc.jibei.util.EchartsToPicUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/4/29 11:06
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Resource
    private ReportInterfaceMapper reportInterfaceMapper;
    @Value("${spring.cfg.interfaceUrl}")
    private String apiBaseUrl;

    @Value("${spring.cfg.pngPath}")
    private String imgPath;

    @Override
    public void generate(JSONObject params) throws IOException, TemplateException {
        // 报告接口列表
        QueryWrapper<ReportInterface> qw = new QueryWrapper<>();
        qw.eq("model_id", params.getString("reportId"));
        List<ReportInterface> reportInterfaces = reportInterfaceMapper.selectList(null);
        // 请求参数
        String paramsStr = JSON.toJSONString(params);

        // 模版数据
        Map<String, Object> ftlData = new HashMap<>();
        String startTime = params.getString("startTime");
        String endTime = params.getString("endTime");
        LocalDate startDate = LocalDate.parse(startTime);
        LocalDate endDate = LocalDate.parse(endTime);

        // 报告周期
        ftlData.put("sYear", startDate.getYear());
        ftlData.put("sMonth", startDate.getMonthValue());
        ftlData.put("sDay", startDate.getDayOfMonth());
        ftlData.put("eYear", endDate.getYear());
        ftlData.put("eMonth", endDate.getMonthValue());
        ftlData.put("eDay", endDate.getDayOfMonth());

        // todo 临时替换图片
        ftlData.put("staticDeviationSchematicImg", pic(imgPath + "img.png")); // 偏航静态偏差示意图
        ftlData.put("staticDeviationImg", pic(imgPath + "13.png")); // 风电机组偏航静态偏差情况统计
        ftlData.put("staticDeviationEmImg", pic(imgPath + "14.png")); // 风电机组偏航缺陷情况
        // todo 查接口，临时代替（偏航）
        this.setYawEvaluation(ftlData);

        reportInterfaces.forEach((api -> {
            log.info("interfaceURL:{}", api.getInterUrl());
            JSONArray jsonArray = this.getArray(api.getInterUrl(), paramsStr);
            for (int i = 0; i < jsonArray.size(); i++) {
                if (Objects.equals(api.getPlaceTag(), "arr")) {
                    this.handleDoubleList(i, jsonArray, ftlData);
                    continue;
                }
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.getString("type");
                String name = jsonObject.getString("name");
                // normal
                if (ValueType.NORMAL.equals(type)) {
                    this.handleNormal(jsonObject, ftlData);
                }
                // table
                if (ValueType.TABLE.equals(type)) {
                   this.handleTable(jsonObject, ftlData);
                }
                // barGroup
                if (ValueType.BAR_GROUP.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
                    String[] yBarName = Convert.toStrArray(value.getJSONArray("yBarName"));
                    JSONArray yData = value.getJSONArray("yData");
                    Double[][] y = new Double[yData.size()][];
                    for (int j = 0; j < yData.size(); j++) {
                        Double[] data = Convert.toDoubleArray(yData.getJSONArray(i));
                        y[j] = data;
                    }
                    String base64Img = EchartsToPicUtil.echartBarGroup(true, yBarName, yBarName, xData, y);
                    ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
                }
                // barGroup
                if (ValueType.BAR.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
                    Double[] yData = Convert.toDoubleArray(value.getJSONArray("yData"));
                    String base64Img = EchartsToPicUtil.echartBar(true, "", xData, yData);
                    ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
                }
                // PIE
                if (ValueType.PIE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    String[] names = Convert.toStrArray(value.getJSONArray("names"));
                    Double[] datas = Convert.toDoubleArray(value.getJSONArray("datas"));
                    String base64Img = EchartsToPicUtil.echartPie(true, "", names, datas);
                    ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
                }
                // STACKED_BARE
                if (ValueType.STACKED_BARE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
                    String[] yName = Convert.toStrArray(value.getJSONArray("yName"));
                    JSONArray yData = value.getJSONArray("yData");
                    Double[][] y = new Double[yData.size()][];
                    for (int j = 0; j < yData.size(); j++) {
                        Double[] data = Convert.toDoubleArray(yData.getJSONArray(i));
                        y[j] = data;
                    }
                    String base64Img = EchartsToPicUtil.echartStackedBare(true, "",xData, yName, y);
                    ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
                }
                // mix
                if (ValueType.MIX.equals(type)) {
                    handleMix(jsonObject, ftlData);
                }
            }
        }));

        // Configuration 用于读取ftl文件
        Configuration configuration = new Configuration(new Version("2.3.9"));
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(new File("/opt/data/ftl"));
        // 以 utf-8 的编码读取ftl文件
        Template template = configuration.getTemplate("1.ftl", "utf-8");
        template.process(ftlData, new FileWriter("/opt/data/ftl/1.docx"));
    }

    private void handleDoubleList(int i, JSONArray jsonArray, Map<String, Object> ftlData) {
        List<Map<String, Object>> list =new ArrayList<>();
        JSONArray jsonArray1 = jsonArray.getJSONArray(i);
        Map<String, Object> map = new HashMap<>();
        for (int i1 = 0; i1 < jsonArray1.size(); i1++) {
            JSONObject jsonObject = jsonArray1.getJSONObject(i1);
            String type = jsonObject.getString("type");
            String name = jsonObject.getString("name");
            Object value = jsonObject.get("value");
            if (ValueType.NORMAL.equals(type)) {
                map.put(name, value);
            }
            if (ValueType.TABLE.equals(type)) {
                map.put(name, value);
            }
        }
        list.add(map);
        ftlData.put("list", list);
    }


    private void handleMix(JSONObject jsonObject, Map<String, Object> ftlData) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        String type = jsonObject.getString("type");
        String name = jsonObject.getString("name");
        Map<String, Object> data = new HashMap<>();
        JSONArray jsonArray = jsonObject.getJSONArray("value");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            data.put(obj.getString("name"), obj.get("value"));
        }
        if (ValueType.NORMAL.equals(type)) {

            Object value = jsonObject.get("value");
            ftlData.put(name, value);
        }

        ftlData.put(name, dataList);
    }

    private void handleTable(JSONObject jsonObject, Map<String, Object> ftlData) {
        String name = jsonObject.getString("name");
        Object value = jsonObject.get("value");
        if (value == null) {
            value = new ArrayList<>();
        }
        ftlData.put(name, value);
    }

    private void handleNormal(JSONObject jsonObject, Map<String, Object> ftlData) {
        String name = jsonObject.getString("name");
        Object value = jsonObject.get("value");
        if (value == null) {
            value = "";
        }
        ftlData.put(name, value);
    }

    private JSONArray getArray(String url, String jsonParam){
        return getJSONObject(url,jsonParam).getJSONArray("data");
    }

    private JSONObject getJSONObject(String url, String jsonParam){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(jsonParam, headers);
        return restTemplate.postForObject(apiBaseUrl + url, request, JSONObject.class);
    }

    private void setYawEvaluation(Map<String, Object> data) {
        // 风电场各风电机组偏航角度及偏航评价列表
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("jzbh", "fj001");
        dataMap.put("phjd", 10);
        dataMap.put("phpj", 234);
        dataList.add(dataMap);
        data.put("yawEvaluationList", dataList);
        // 小节
        data.put("xjBestPerfJzbh", "fj001");
        data.put("xjWorstPerfJzbh", "fj002");
        data.put("xjNormalYawCount", 12);
        data.put("xjNormalYawRate", 10);
        data.put("xjWarnYawCount", 11);
        data.put("xjWarnYawRate", 10);
    }

    public static String pic(String path)  {
        byte[] bytes = FileUtil.readBytes(new File(path));
        return Base64.encode(bytes);
    }

}
