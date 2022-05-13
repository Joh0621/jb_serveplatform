package com.bonc.jibei.service.Impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.jibei.api.ValueType;
import com.bonc.jibei.config.WordCfgProperties;
import com.bonc.jibei.entity.ImgData;
import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.mapper.ReportInterfaceMapper;
import com.bonc.jibei.mapper.ReportModelInterMapper;
import com.bonc.jibei.service.ReportService;
import com.bonc.jibei.util.EchartsToPicUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

//    private final RestTemplate restTemplate = new RestTemplate();

    @Resource
    private WordCfgProperties wordCfgProperties;
    @Resource
    private ReportInterfaceMapper reportInterfaceMapper;
    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    @Override
    public String generate(JSONObject params) throws IOException, TemplateException {
        //取得场站模板接口列表
        Integer modelId = params.getInteger("modelId");
        // 报告接口列表
        List<ReportInterface> reportInterfaces = reportModelInterMapper.selectReportInter(modelId);

        // 接口请求参数
        params.put("reportId", modelId);
        params.remove("modelId"); // 删除该参数

        // 模版数据
        Map<String, Object> ftlData = new HashMap<>();

        ftlData.put("defaultValue", "--");

        ftlData.put("staticDeviationImg", ""); // 风电机组偏航静态偏差情况统计
        ftlData.put("staticDeviationEmImg", ""); // 风电机组偏航缺陷情况

        reportInterfaces.forEach((api -> {
            log.info("interfaceURL:{}", api.getInterUrl());
            if ("/api/getStationDeviceSyntheticalSummary".equals(api.getInterUrl())) {
                ftlData.put("deviceAppendixMix", new ArrayList<>());
                return;
            }
            JSONArray jsonArray = this.getArray(api.getInterUrl(), params);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.getString("type");
                String name = jsonObject.getString("name");
                // normal
                if (ValueType.NORMAL.equals(type)) {
                    Object value = jsonObject.get("value");
                    this.handleNormal(name, value, ftlData);
                }
                // table
                if (ValueType.TABLE.equals(type)) {
                    JSONArray value = jsonObject.getJSONArray("value");
                    this.handleTable(name, value, ftlData);
                }
                // BAR
                if (ValueType.BAR.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handleBar(name, value, ftlData);
                }
                // barGroup
                if (ValueType.BAR_GROUP.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handleBarGroup(name, value, ftlData);
                }
                // PIE
                if (ValueType.PIE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handlePie(name, value, ftlData);
                }
                // STACKED_BARE
                if (ValueType.STACKED_BARE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.HandleStackedBare(name, value, ftlData);
                }
                if (ValueType.LINE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handleLine(name, value, ftlData);
                }
                // mix
                if (ValueType.MIX.equals(type)) {
                    JSONArray value = jsonObject.getJSONArray("value");
                    handleMix(name, value, ftlData);
                }
            }
        }));
//        String s = FileUtil.readString("D:/data.json", StandardCharsets.UTF_8);
//        ftlData = JSON.parseObject(s);
        // Configuration 用于读取ftl文件

        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setDirectoryForTemplateLoading(new File(wordCfgProperties.getModelPath()));
        // 以 utf-8 的编码读取ftl文件
        Template template = configuration.getTemplate("1.ftl", StandardCharsets.UTF_8.name());
        String fileName = wordCfgProperties.getWordPath() + params.getString("stationId") + ".docx";
        FileUtil.writeString(JSON.toJSONString(ftlData), new File("/data/gb3q/data.json"), StandardCharsets.UTF_8.name());
        template.process(ftlData, new FileWriter(fileName));
        return fileName;
    }

    private void handlePie(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] names = Convert.toStrArray(value.getJSONArray("names"));
        Double[] datas = Convert.toDoubleArray(value.getJSONArray("datas"));
        String base64Img = EchartsToPicUtil.echartPie(true, "", names, datas);
        ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
    }

    private void handleBar(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        Double[] yData = Convert.toDoubleArray(value.getJSONArray("yData"));
        String base64Img = EchartsToPicUtil.echartBar(true, "", xData, yData);
        ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
    }

    private void handleBarGroup(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        String[] yBarName = Convert.toStrArray(value.getJSONArray("yBarName"));
        JSONArray yData = value.getJSONArray("yData");
        Double[][] y = new Double[yData.size()][];
        for (int j = 0; j < yData.size(); j++) {
            Double[] data = Convert.toDoubleArray(yData.getJSONArray(j));
            y[j] = data;
        }
        String base64Img = EchartsToPicUtil.echartBarGroup(true, yBarName, yBarName, xData, y);
        ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
    }

    private void handleMix(String rootName, JSONArray rootValue, Map<String, Object> ftlData) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < rootValue.size(); i++) {
            JSONArray jsonArray = rootValue.getJSONArray(i);
            Map<String, Object> data = new HashMap<>();
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                String type = jsonObject.getString("type");
                String name = jsonObject.getString("name");

                if (ValueType.NORMAL.equals(type)) {
                    Object value = jsonObject.get("value");
                    this.handleNormal(name, value, data);
                }

                if (ValueType.TABLE.equals(type)) {
                    JSONArray value = jsonObject.getJSONArray("value");
                    this.handleTable(name, value, data);
                }

                if (ValueType.PIE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handlePie(name, value, data);
                }

                if (ValueType.BAR.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handleBar(name, value, data);
                }
                // barGroup
                if (ValueType.BAR_GROUP.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handleBarGroup(name, value, data);
                }
                if (ValueType.LINE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handleLine(name, value, data);
                }
                if (ValueType.RADAR.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.handleRadar(name, value, data);
                }   // STACKED_BARE
                if (ValueType.STACKED_BARE.equals(type)) {
                    JSONObject value = jsonObject.getJSONObject("value");
                    this.HandleStackedBare(name, value, data);
                }
            }
            dataList.add(data);
        }
        ftlData.put(rootName, dataList);
    }

    private void HandleStackedBare(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        String[] yName = Convert.toStrArray(value.getJSONArray("yName"));
        JSONArray yData = value.getJSONArray("yData");
        Double[][] y = new Double[yData.size()][];
        for (int j = 0; j < yData.size(); j++) {
            Double[] data = Convert.toDoubleArray(yData.getJSONArray(j));
            y[j] = data;
        }
        String base64Img = EchartsToPicUtil.echartStackedBare(true, "", xData, yName, y);
        ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
    }

    private void handleRadar(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        String[] yNames = Convert.toStrArray(value.getJSONArray("yName"));
        Double[] yData =  Convert.toDoubleArray(value.getJSONArray("yData"));
        String[] titles = {};
        String base64Img = EchartsToPicUtil.echartRadar(true, titles, xData, yData, yNames);
        ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
    }

    private void handleLine(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        String[] yNames = Convert.toStrArray(value.getJSONArray("yName"));
        JSONArray yData = value.getJSONArray("yData");
        Double[][] y = new Double[yData.size()][];
        for (int j = 0; j < yData.size(); j++) {
            Double[] data = Convert.toDoubleArray(yData.getJSONArray(j));
            y[j] = data;
        }
        String base64Img = EchartsToPicUtil.echartLine(true, "", yNames, xData, y);
        ftlData.put(name, new ImgData(UUID.randomUUID().toString(), base64Img));
    }

    private void handleTable(String name, JSONArray value, Map<String, Object> ftlData) {
        if (value == null) {
            value = new JSONArray();
        }
        ftlData.put(name, value);
    }

    private void handleNormal(String name, Object value, Map<String, Object> ftlData) {
        if (value == null) {
            value = "";
        }
        ftlData.put(name, value);
    }

    private JSONArray getArray(String url, JSONObject jsonParam) {
        return getJSONObject(url, jsonParam).getJSONArray("data");
    }

    private JSONObject getJSONObject(String url, JSONObject jsonParam) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        HttpEntity<JSONObject> request = new HttpEntity<>(jsonParam, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(wordCfgProperties.getInterfaceUrl() + url, request, JSONObject.class);
    }

    public static String pic(String path) {
        byte[] bytes = FileUtil.readBytes(new File(path));
        return Base64.encode(bytes);
    }

}
