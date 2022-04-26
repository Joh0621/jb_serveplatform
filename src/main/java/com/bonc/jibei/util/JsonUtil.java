package com.bonc.jibei.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/25 14:22
 * @Description: TODO
 */
public class JsonUtil {
    public static Map<String,Object> getMap(String urlStr){
        //JSONObject
        return null;
    }
    public static JSONObject createJson(Integer stationId,Integer typeId,String starttime,String endtime){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("stationId",stationId);
        jsonObject.put("typeId",typeId);
        jsonObject.put("startTime",starttime);
        jsonObject.put("endTime",endtime);
        return jsonObject;
    }
}
