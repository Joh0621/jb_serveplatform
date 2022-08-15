package com.bonc.jibei.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/25 14:22
 * @Description: TODO
 */
public class JsonUtil {
    public static Map<String, Object> getMap(String urlStr) {
        return null;
    }

    /**
     * param,stationId:场站ID；typeId：场站类型ID;modelId:模板ID;starttime:开始时间；endtime：捷顺时间
     **/
    public static JSONObject createJson(Integer stationId, Integer typeId, Integer modelId, String starttime, String endtime, Integer reportId,String resReportName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("stationId", stationId);
        jsonObject.put("typeId", typeId);
        jsonObject.put("modelId", modelId);
        jsonObject.put("startTime", starttime);
        jsonObject.put("endTime", endtime);
        jsonObject.put("reportId", reportId);
        jsonObject.put("resReportName", resReportName);
        return jsonObject;
    }
}
