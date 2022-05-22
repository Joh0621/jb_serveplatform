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
import com.bonc.jibei.service.ReportPoiTlService;
import com.bonc.jibei.service.ReportService;
import com.bonc.jibei.util.EchartsToPicUtil;
import com.bonc.jibei.util.PoiTLUtils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.render.RenderContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
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
public class ReportPoiTlServiceImpl implements ReportPoiTlService {

//    private final RestTemplate restTemplate = new RestTemplate();

    @Resource
    private WordCfgProperties wordCfgProperties;
    @Resource
    private ReportModelInterMapper reportModelInterMapper;

    @Override
    public String generate(JSONObject params) throws IOException, TemplateException {
        LoopRowTableRenderPolicy loopRowTableRenderPolicy = new LoopRowTableRenderPolicy();
        ConfigureBuilder builder = Configure.builder()
                .useSpringEL(true); // 开启 springEL 表达式

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
                    bindLoopRowTablePolicy(builder, name, loopRowTableRenderPolicy);
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
                    handleMix(name, value, ftlData, builder, loopRowTableRenderPolicy);
                }
            }
        }));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:/data/obj.data"));
        oos.writeObject(ftlData);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("D:/data/obj.data"));
        try {
            Map<String, Object> o = (Map<String, Object>)ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        String s = FileUtil.readString("D:/data.json", StandardCharsets.UTF_8);
//        ftlData = JSON.parseObject(s);
        // Configuration 用于读取ftl文件

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=======================render start ====================");
            try {
                XWPFTemplate.compile(wordCfgProperties.getModelPath() + "/XXX风电场运行性能评估分析报告模板V1版本.docx", builder.build())
                        .render(ftlData)
                        .writeToFile(wordCfgProperties.getWordPath() + params.getString("stationId") + ".docx");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            System.out.println("=======================render end ====================");
        }


    }

    private void handlePie(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] names = Convert.toStrArray(value.getJSONArray("names"));
        Double[] datas = Convert.toDoubleArray(value.getJSONArray("datas"));
        String path = EchartsToPicUtil.echartPie(true, "", names, datas);
        ftlData.put(name, PoiTLUtils.picData(path));
    }

    private void handleBar(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        Double[] yData = Convert.toDoubleArray(value.getJSONArray("yData"));
        String path = EchartsToPicUtil.echartBar(true, "", xData, yData);
        ftlData.put(name, PoiTLUtils.picData(path));
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
        String path = EchartsToPicUtil.echartBarGroup(true, yBarName, yBarName, xData, y);
        ftlData.put(name, PoiTLUtils.picData(path));
    }

    private void handleMix(String rootName, JSONArray rootValue, Map<String, Object> ftlData, ConfigureBuilder builder, RenderPolicy policy) {
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
                    bindLoopRowTablePolicy(builder, name, policy);
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
        String path = EchartsToPicUtil.echartStackedBare(true, "", xData, yName, y);
        ftlData.put(name, PoiTLUtils.picData(path));
    }

    private void handleRadar(String name, JSONObject value, Map<String, Object> ftlData) {
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        String[] yNames = Convert.toStrArray(value.getJSONArray("yName"));
        Double[] yData =  Convert.toDoubleArray(value.getJSONArray("yData"));
        String[] titles = {};
        String path = EchartsToPicUtil.echartRadar(true, titles, xData, yData, yNames);
        ftlData.put(name, PoiTLUtils.picData(path));
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
        String path = EchartsToPicUtil.echartLine(true, "", yNames, xData, y);
        ftlData.put(name, PoiTLUtils.picData(path));
    }

    private void handleTable(String name, JSONArray value, Map<String, Object> ftlData) {
        if (value == null) {
            value = new JSONArray();
        }
        ftlData.put(name, value);
    }

    private void handleNormal(String name, Object value, Map<String, Object> ftlData) {
        if (value == null) {
            value = "--";
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

    /**
     * 绑定表格行循环插件
     * @param builder builder
     * @param tagName tagName
     */
    private void bindLoopRowTablePolicy(ConfigureBuilder builder, String tagName, RenderPolicy policy){
        builder.bind(tagName, policy);
    }

}
