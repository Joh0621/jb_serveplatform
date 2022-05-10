package com.bonc.jibei.service.Impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bonc.jibei.api.ValueType;
import com.bonc.jibei.entity.ImgData;
import com.bonc.jibei.entity.ReportInterface;
import com.bonc.jibei.entity.User;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.hutool.core.convert.Convert;

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

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.stream()
                .collect(Collectors.groupingBy((Function<User, Object>) User::getUserName))
                .forEach((key, val) -> {
                    List<User> collect = val.stream().sorted().limit(10).collect(Collectors.toList());
                });
    }

    @Resource
    private ReportInterfaceMapper reportInterfaceMapper;
    @Value("${spring.cfg.interfaceUrl}")
    private String apiBaseUrl;

    private String imgPath = "/opt/data/ftl/img/";

    @Override
    public void generate(int reportId) throws IOException, TemplateException {
        // 报告接口列表
        QueryWrapper<ReportInterface> qw = new QueryWrapper<>();
        qw.eq("report_id", reportId);
        List<ReportInterface> reportInterfaces = reportInterfaceMapper.selectList(null);
        // 请求参数
        JSONObject params = new JSONObject();
        params.put("reportId", reportId);
        params.put("stationId", 934);
        params.put("typeId", 1);
        params.put("startTime", "2022-01-01");
        params.put("endTime", "2022-04-01");
        String paramsStr = JSON.toJSONString(params);

        // 模版数据
        Map<String, Object> ftlData = new HashMap<>();
        // 报告周期
        ftlData.put("sYear", "2022");
        ftlData.put("sMonth", 1);
        ftlData.put("sDay", 1);
        ftlData.put("eYear", "2022");
        ftlData.put("eMonth", 4);
        ftlData.put("eDay", 1);

        ftlData.put("staticDeviationSchematicImg", pic(imgPath + "img.png")); // 偏航静态偏差示意图
        ftlData.put("staticDeviationImg", pic(imgPath + "13.png")); // 风电机组偏航静态偏差情况统计
        ftlData.put("staticDeviationEmImg", pic(imgPath + "14.png")); // 风电机组偏航缺陷情况

        // todo 查接口，临时代替

        this.setYawEvaluation(ftlData);
        reportInterfaces.forEach((api -> {
            log.info("interfaceURL:{}", api.getInterUrl());
            JSONArray jsonArray = this.getArray(api.getInterUrl(), paramsStr);
            for (int i = 0; i < jsonArray.size(); i++) {
                if (Objects.equals(api.getPlaceTag(), "arr")) {
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


    private void agCPerformanceEvaluationAnalysis(Map<String, Object> data) {
        data.put("agcYear", "2022");
        data.put("agcPermImg1", pic(imgPath + "15.png"));
        data.put("agcPermImg2", pic(imgPath + "16.png"));
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

    private void setYawStaticBiasAnalysis(Map<String, Object> data) {
        data.put("staticDeviationSchematicImg", pic(imgPath + "img.png")); // 偏航静态偏差示意图
        data.put("staticDeviationDistributionRange", 100); // 静态偏差分布范围
        data.put("staticAvgDeviation", 100); // 平均偏差值
        data.put("staticDeviationImg", pic(imgPath + "13.png")); // 风电机组偏航静态偏差情况统计
        data.put("staticDeviationJzCount",12); // 存在偏航静态偏差缺陷的机组数量
        data.put("staticPoorDeviationJz", "fj001"); // 偏航静态偏差最大机组
        data.put("staticPoorDeviation", 10); // 偏差
        data.put("staticDeviationEmImg", pic(imgPath + "14.png")); // 风电机组偏航缺陷情况
    }

    private void setPowerGenerationPerformanceConsistencyAnalysis(Map<String, Object> data) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("deviceXh", "10001"); // 设备型号
        dataMap.put("fgImg", pic(imgPath + "12.png")); // 机组实际运行的最优功率曲线与最差功率曲线
        dataMap.put("optimalJz", "fj001"); // 最优机组
        dataMap.put("worstJz", "fj002"); // 最差机组
        // 机组发电能力指标Fg（正序)
        List<Map<String, Object>> fgList = new ArrayList<>();
        Map<String, Object> fg = new HashMap<>();
        fg.put("jzbh", "fj002");
        fg.put("fg", 23);
        fg.put("sort", 100);
        fgList.add(fg);
        dataMap.put("list", fgList);
        dataList.add(dataMap);
        data.put("fgList", dataList);
    }

    private void setPowerGenerationPerformanceFgAnalysis(Map<String, Object> data) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("deviceXh", "10001"); // 设备型号
        dataMap.put("bestWorstPowerImg", pic(imgPath + "10.png")); // 发电能力指标统计
        dataMap.put("excellentCount", 10); // 优
        dataMap.put("excellentRate", 10); // 优占比
        dataMap.put("goodCount", 10); // 良
        dataMap.put("goodRate", 10); // 良占比
        dataMap.put("moderateCount", 10); // 中
        dataMap.put("moderateRate", 10); // 中占比
        dataMap.put("poorCount", 10); // 差
        dataMap.put("poorRate", 10); // 差占比
        dataMap.put("powerRateImg", pic(imgPath + "11.png")); // 风电机组功率曲线分类占比饼状图

        // 风电机组的功率曲线分类列表
        List<Map<String, Object>> powerList = new ArrayList<>();
        Map<String, Object> power = new HashMap<>();
        power.put("stationName", "迎风铃");
        power.put("deviceXh", "10001");
        power.put("deviceNo", "fj001");
        power.put("category", "优");
        powerList.add(power);
        dataMap.put("powerList", powerList);
        dataList.add(dataMap);
        data.put("pgpcAnalysisList", dataList);
    }


    private void setLostPowerAnalysis(Map<String, Object> data) {
        // 损失电量前五名
//        List<Map<String, Object>> dataList = new ArrayList<>();
//        Map<String, Object> dataMap = new HashMap<>();
//        dataMap.put("reason", "不可抗力损失电量"); // 损失原因
//        dataMap.put("proportion", 10); // 占比
//        dataList.add(dataMap);
//        data.put("lostPowerRankList", dataList);
        Map<String, Object> params = new HashMap<>();
        params.put("stationId", 610);
        params.put("typeId", 1);
        params.put("startTime", "2022-01-01");
        params.put("endTime", "2022-04-01");
        JSONArray result = getJSONArray("/api/getStationLosePowerTop5", JSON.toJSONString(params));
        data.put("lostPowerRankList", result);

        data.put("lostPowerImg", pic(imgPath + "8.png")); // 损失电量占比图
        data.put("monthLostPowerImg", pic(imgPath + "9.png")); // 损失电量按照月度统计

        // 小节
        data.put("xjMaxEleMonth", 4);// 最高发电量月份
        data.put("xjMaxMonthEle", 3423);// 发电量
        data.put("xjMaxEleJz", "fj001");// 发电量最高机组
        data.put("xjMinEleJz", "fj002");// 机组发电量最低
        data.put("xjJzEleDeviation", 223);// 机组间发电量的最大偏差为

        data.put("xjRhMaxMonth", 12);// 正常运行小时数最高月份
        data.put("xjNomalRhRate", 12);// 正常运行小时占比
        data.put("xjXglRhRate", 11);// 本季度限功率运行小数占
        data.put("xjJztjRate", 22);// 机组停机时间占比
        data.put("xjTjzcyn", "极端天气");// 机组停机时间最长的原因
        data.put("xjTjzcynRate", 23);// 导致机组停机时间最长的原因的占比

        data.put("xjMostFaultPart", "叶片");// 故障次数最多部件
        data.put("xjMostFaultPartCount", 10);// 发生故障多少次
        data.put("xjMostFaultCountJz", "jz001");// 故障次数最多机组
        data.put("xjLongestFaultTimeJz", "fj001");// 最长故障累计故障时间机组
        data.put("xjAvgNoFaultRuntime", 23);// 平均无故障运行时间
        data.put("xjUseRate", 10);// 可利用率
        data.put("xjHighestUseRateJz", "fj001");// 平均可利用率最高机组
        data.put("xjHighestUseRate", 10);// 平均可利用率
        data.put("xjLowestUseRateJz", "fj001");// 平均可利用率差高机组
        data.put("xjLowestUseRate", 10);// 平均可利用率最高机组
        data.put("xjHighestLostPowerReason", 10);// 损失电量占比最高原因



    }

    private void setMaintenanceIndexAnalysis(Map<String, Object> data) {
        // 各台机组检修指标情况统计
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("jzbh", "fj001"); // 机组编号
        dataMap.put("avgMaintenanceIntervalTime", 10); // 检修故障平均间隔时间
        dataMap.put("avgMaintenanceRepairTime", 19);// 检修故障平均修复时间
        dataList.add(dataMap);
        data.put("maintenanceIndexList", dataList);
    }

    private void setFailureIndexAnalysis(Map<String, Object> data) {
        // 各台机组故障指标情况统计列表
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("jzbh", "fj001"); // 机组编号
        dataMap.put("avgNoFaultRunTime", 10); // 平均无故障运行时间
        dataMap.put("avgRage", 19);// 平均利用率
        dataList.add(dataMap);
        data.put("faultIndexList", dataList);
    }

    private void setFailureStatistics(Map<String, Object> data) {
        data.put("faultCount", 10); // 故障次数
        data.put("part", "轴承");  // 故障部件
        data.put("partFaultCount", 10);// 部件故障次数
        data.put("faultTimePart", "扇叶片"); // 故障时长部件
        data.put("faultTime", 10); // 故障时长

        data.put("faultCountImg", pic(imgPath + "6.png")); // 故障次数统计图
        data.put("faultTimeImg", pic(imgPath + "7.png")); // 故障累积时长图
        data.put("faultCode", "故障代码");// 故障代码
        data.put("faultCodeCount", 10); // 故障代码次数
        data.put("timeFaultCode", "故障代码"); // 故障时长最长的故障代码
        data.put("faultCodeTime", 10); // 故障时长
        // 风电场频发故障列表
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("faultCodeName", "faultCodeName"); // 故障代码名称
        dataMap.put("faultCodeCount", 10); // 发生的次数
        dataMap.put("faultCodeTimeName", "faultCodeName");// 累计时长故障代码名称
        dataMap.put("faultCodeTime", 10); // 累计时长
        dataList.add(dataMap);
        data.put("faultList", dataList);
        data.put("mostFaultJz", "fj001"); // 最多故障机组
        data.put("mostFaultCount", 10); // 故障次数
        data.put("mostFaultTimeJz", "fj001"); // 累计时长机组
        data.put("mostFaultTime", 10); // 累计时长

        // 故障次数TOP10机组分析列表
        List<Map<String, Object>> dataList2 = new ArrayList<>();
        Map<String, Object> dataMap2 = new HashMap<>();
        dataMap2.put("sort", 1); // 排名
        dataMap2.put("jzbh", 10); // 机组编号
        dataMap2.put("jzFaultCount", 10);// 机组故常次数
        dataList2.add(dataMap2);
        data.put("faultCountList", dataList2);

        // 故障累计时长TOP10机组分析列表
        List<Map<String, Object>> dataList3 = new ArrayList<>();
        Map<String, Object> dataMap3 = new HashMap<>();
        dataMap3.put("sort", 1); // 排名
        dataMap3.put("jzbh", 10); // 机组编号
        dataMap3.put("jzFaultTime", 10);// 机组故常次数
        dataList3.add(dataMap3);
        data.put("faultTimeList", dataList3);
    }

    private void setRuntimeHourAnalysis(Map<String, Object> data) {
        // 正常发电小时数占比
        data.put("zcfd", "10");
        data.put("znpt", "10");
        data.put("dwxgl", "10");
        data.put("jzgztj", "10");
        data.put("djsc", "10");
        data.put("jzlxwh", "10");
        data.put("rtOther", "10");
        // 风电场运行小时数分析图
        data.put("runTimeHourImg", pic(imgPath + "4.png"));
        // 季度各月损失运行时间、并网发电时间、待机时间的统计如下图所示。
        data.put("ssBwDjImg",  pic(imgPath + "5.png"));
        // 各台机组运行小时数分析
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("jzbh", "fj001");
        dataMap.put("znpt", "10");
        dataMap.put("zwpt", "10");
        dataMap.put("jzgztj", "10");
        dataMap.put("jzlxwh", "10");
        dataMap.put("other", "10");
        dataMap.put("zcfd", "10");
        dataMap.put("dwxgl", "10");
        dataMap.put("djsj", "10");
        dataList.add(dataMap);
        data.put("rtHourAnalysisList", dataList);
    }

    private void setPowerGenerationRanking(Map<String, Object> data) {
        Map<String, Object> params = new HashMap<>();
        params.put("stationId", 610);
        params.put("typeId", 1);
        params.put("startTime", "2022-01-01");
        params.put("endTime", "2022-04-01");
        JSONArray jsonArray = this.getJSONArray("/api/getDeviceGenerationRank", JSON.toJSONString(params));
        data.put("deviceGenerationByUnit", jsonArray);
    }

    private void setMonthlyPowerAnalysis(Map<String, Object> data) {
        data.put("euh", 123.2);
        data.put("areaSort", 1);
        data.put("city", "张北");
        data.put("citySort", 2);
        data.put("totalEle", "234234.32");

        //todo 发电量月度分析
        data.put("monthEleImgName",  "1.png");
        data.put("monthEleImg",  pic(imgPath + "1.png"));
        // 最优机组
        data.put("optimalUnit", "001");
        data.put("optimalUnitEle", "872348");
        data.put("worstUnit", "872348");
        data.put("worstUnitEle", "872348");
        data.put("maxDeviation", "872348");
        // 发电量前十名机组统计
        data.put("top10UnitImg", pic(imgPath + "2.png"));
        // 发电量后十名机组统计
        data.put("bottom10UnitImg", pic(imgPath + "3.png"));
    }

    /**
     * 设置基本信息，场站名称，装机容量，设备数量
     */
    private void setBasicInfo(Map<String, Object> data) {
        Map<String, Object> params = new HashMap<>();
        params.put("stationId", 610);
        params.put("typeId", 1);

        JSONObject result = this.getJSONObject("/api/getStationInfo", JSON.toJSONString(params));
        data.put("stationName", result.getString("stationName"));
        data.put("stationCapacity", result.getIntValue("stationCapacity"));
        data.put("stationDeviceNum", result.getIntValue("stationDeviceNum"));
        data.put("year", String.valueOf(LocalDate.now().getYear()));
        data.put("month", LocalDate.now().getMonthValue());
        // todo
        data.put("quarter", "一");
        // todo 报告周期
        data.put("sYear", "2022");
        data.put("sMonth", 1);
        data.put("sDay", 1);
        data.put("eYear", "2022");
        data.put("eMonth", 4);
        data.put("eDay", 1);
    }

    private JSONArray getJSONArray(String url, String jsonParam){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(jsonParam, headers);
        JSONObject jsonObject = restTemplate.postForObject(apiBaseUrl + url, request, JSONObject.class);
        return jsonObject.getJSONArray("data");
    }


    public static String pic(String path)  {
        byte[] bytes = FileUtil.readBytes(new File(path));
        return Base64.encode(bytes);
    }

}
