package com.bonc.jibei.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/24 22:22
 * @Description: TODO
 */
public class HttpUtil {
    private static final String CODE_UTF8 = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection(); // 打开连接
        System.out.println(urlConnection.getURL().toString());
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        br.close();
        System.out.println(sb.toString());
        return  sb.toString();
    }
    public static JSONObject httpPost(String url, JSONObject jsonParam) {
        JSONObject jsonResult = null;
        // 创建httppost
        // 创建默认的httpClient实例.
        HttpPost httppost = new HttpPost(url);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            if (null != jsonParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), CODE_UTF8);
                entity.setContentEncoding(CODE_UTF8);
                entity.setContentType("application/json");
                httppost.setEntity(entity);
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            jsonResult = getResponse(response, url);
        } catch (ClientProtocolException | UnsupportedEncodingException e) {
            logger.error("错误 :" + e);
        } catch (IOException e) {
           logger.error("IOException 错误 :" + e);
        }
        return jsonResult;
    }
    public static JSONObject getResponse(CloseableHttpResponse response, String url) {
        JSONObject jsonResult = null;
        String urls = null;
        try {
            /** 请求发送成功，并得到响应 **/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /** 读取服务器返回过来的json字符串数据 **/
                String strResult = EntityUtils.toString(response.getEntity());
                /** 把json字符串转换成json对象 **/
                jsonResult = JSONObject.parseObject(strResult);
                urls = URLDecoder.decode(url, CODE_UTF8);
            } else {
                logger.error("err:"+urls);
            }
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            try {
                response.close();
            } catch (IOException e) {
               logger.error(e.toString());
            }
        }
        return jsonResult;
    }
}
