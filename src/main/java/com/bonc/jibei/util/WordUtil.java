package com.bonc.jibei.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bonc.jibei.mapper.UserMapper;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.*;

import javax.annotation.Resource;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: dupengling
 * @DateTime: 2022/4/20 18:39
 * @Description: TODO
 */
public class WordUtil {
    @Resource
    UserMapper userMapper;
    /**
     * 替换段落里面的变量
     * @param doc 要替换的文档
     * @param params 参数
     */
    private void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            this.replaceInPara(para, params);
        }
    }
    /**
     * 替换段落里面的变量
     * @param para     要替换的段落
     * @param params     参数
     */
    private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        Matcher matcher;
        if (this.matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            for (int i = 0; i < runs.size(); i++) {
                XWPFRun run = runs.get(i);
                String runText = run.toString();
                matcher = this.matcher(runText);
                if (matcher.find()) {
                    while ((matcher = this.matcher(runText)).find()) {
                        runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                    }
                    // 直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
                    // 所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
                    para.removeRun(i);
                    if(runText.equals("null")){
                        runText="";
                    }
                    para.insertNewRun(i).setText(runText);
                }
            }
        }
    }

    /**
     * 替换表格里面的变量
     * @param doc     要替换的文档
     * @param params     参数
     */
    private void replaceInTable(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {

                    String cellTextString = cell.getText();
                    for (Map.Entry<String, Object> e : params.entrySet()) {
                        if (cellTextString.contains("${"+e.getKey()+"}")) {
                            cellTextString = cellTextString.replace("${" + e.getKey() + "}", e.getValue().toString());
                        }
                    }
                    cell.removeParagraph(0);
                    if(cellTextString.contains("${") && cellTextString.contains("}")){
                        cellTextString = "";
                    }
                    cell.setText(cellTextString);
//                    paras = cell.getParagraphs();
//                    for (XWPFParagraph para : paras) {
//                        this.replaceInPara(para, params);
//                    }

                }
            }
        }
    }

    /**
     * 正则匹配字符串
     * @param str
     * @return
     */
    private Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 关闭输出流
     * @param os
     */
    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 输出CoreProperties信息
     * @param coreProps
     */
    private void printCoreProperties(POIXMLProperties.CoreProperties coreProps) {
        System.out.println(coreProps.getCategory()); // 分类
        System.out.println(coreProps.getCreator()); // 创建者
        System.out.println(coreProps.getCreated()); // 创建时间
        System.out.println(coreProps.getTitle()); // 标题
    }
    /**
     * word占位用${object}有缺陷不能填充图片
     * @param filePath
     * @param params
     * @throws Exception
     */
    public static String templateWrite(String filePath, Map<String, Object> params,String outFilePath)throws Exception{
        InputStream is = new FileInputStream(filePath);
        WordUtil writeWordUtil = new WordUtil();
        XWPFDocument doc = new XWPFDocument(is);
        // 替换段落里面的变量
        writeWordUtil.replaceInPara(doc, params);
        // 替换表格里面的变量
        writeWordUtil.replaceInTable(doc, params);
        OutputStream os = new FileOutputStream(outFilePath);
        doc.write(os);
        writeWordUtil.close(os);
        writeWordUtil.close(is);
        os.flush();
        os.close();
        return "";
    }
    /**
     * word占位用{{object}}比较完美可以填充图片
     * @param filePath
     * @param params
     * @param outFilePath
     * @return
     * @throws Exception
     */
    public static String templateWrite2(String filePath, Map<String, Object> params,String outFilePath)throws Exception{
        XWPFTemplate template = XWPFTemplate.compile(filePath).render(params);
        FileOutputStream out = new FileOutputStream(outFilePath);
        template.write(out);
        out.flush();
        out.close();
        template.close();
        return "";
    }
    public static void  writeWord(String  jsonStr) {
         jsonStr = "{\"success\":true," +
                " \"message\":\"ok\", " +
                 " \"code\":\"0\", " +
                "\"data\":[" +
                "{" +
                 "header:{age:12}" +
                 "}" +
                "]}";
        /**

         jsonStr = "{\"success\":true," +
         " \"message\":\"ok\", " +
         " \"code\":\"0\", " +
         "\"data\":[" +
         "" +
         "{'age':12}" +
         ",{'name':'mmm',age:12}" +
         "]}";

         */
        //转换成为JSONObject对象
        JSONObject jsonObj =JSONObject.parseObject(jsonStr) ;

        JSONArray arr=jsonObj.getJSONArray("data");
        JSONObject arr1=arr.getJSONObject(0);
        JSONObject obj=arr1.getJSONObject("header");
        for (String o:obj.keySet()){
            String key1=o;
            String v=obj.getString(o);
            System.out.println("ggg");
        }
    }
    /**
     * 往word文档填充数据ss
     */
    public static void  writeWord() {
        HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
        Configure config = Configure.builder().bind("list", policy).build();
        List<Map> list= Lists.newArrayList();
        for (int i = 1; i < 5; i++) {
            Map data = new HashMap<>();//通过map存放要填充的数据
            data.put("year","2021");
            data.put("quarter",i);
            data.put("num",33+i);
            data.put("num1",33+i);
            list.add(data);
        }
        for (int i = 1; i < 5; i++) {
            Map data = new HashMap<>();//通过map存放要填充的数据
            data.put("year","2022");
            data.put("quarter",i);
            data.put("num",33+i*20);
            data.put("num1",33+i*30);
            list.add(data);
        }
        Map data = new HashMap<>();//通过map存放要填充的数据
        data.put("list",list);

        data.put("title", "河北风电场业绩报表");
        data.put("userName", "JUVENILESS");
        data.put("time", "2018-03-24");
        data.put("sex", "男");

        data.put("saignuser", "男男男");
        data.put("date", "2022-05-01");

        try {
            data.put("encharts", new PictureRenderData(500, 300, PictureType.PNG, BytePictureUtils.toByteArray(new FileInputStream("D:\\echarts\\df27f3fc.png"))));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XWPFTemplate template = XWPFTemplate.compile("D:\\test\\ccc.docx",config).render(data);//调用模板，填充数据
        try {
            FileOutputStream out = new FileOutputStream("d:\\test\\test.docx");//要导出的文件名
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
       // WordUtil.writeWord("");
        JSONObject jsonstr=JsonUtil.createJson(1,2,"2022-01-01 00:00:00","2022-02-02 23:59:59");
        JSONObject jsonstr2=JsonUtil.createJson(1,2,"2022-01-01 00:00:00","2022-02-02 23:59:59");

        List<Object> maplist= Lists.newArrayList();
        maplist.add(jsonstr);
        maplist.add(jsonstr2);
        String json = JSON.toJSONString(maplist);
        List<JSONObject> jsonObjects = JSON.parseArray(json, JSONObject.class);
        for (JSONObject jsonObject : jsonObjects) {
            jsonObject.forEach((key, value) -> {
                System.out.println("key = " + key + ",value=" + value);
            });

            System.out.println("===================");
        }


    }

}
