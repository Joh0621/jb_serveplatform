//package com.bonc.jibei.util;
//
//import net.sf.jxls.transformer.XLSTransformer;
//import org.apache.poi.ss.usermodel.Workbook;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URLEncoder;
//import java.util.Map;
//
//public class TemplateExcelUtils {
//
//    /**
//     * 根据模板导出数据
//     * @param fileName
//     * @param sourcePath resource/template文件夹下路径
//     * @param beanParams
//     * @param response
//     * @throws Exception
//     */
//    public static void downLoadExcel(String fileName,String sourcePath, Map<String, Object> beanParams, HttpServletResponse response)
//            throws Exception {
//        try{
//            OutputStream os = getOutputStream(fileName,response);
//            //读取模板
//            InputStream is = TemplateExcelUtils.class.getClassLoader().getResourceAsStream("test/"+sourcePath);
//            XLSTransformer transformer = new XLSTransformer();
//            //向模板中写入内容
//            Workbook workbook = transformer.transformXLS(is, beanParams);
//            //写入成功后转化为输出流
//            workbook.write(os);
//        }catch (Exception e){
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    /**
//     * 导出文件时为Writer生成OutputStream.
//     * @param fileName 文件名
//     * @param response response
//     * @return ""
//     */
//    private static OutputStream getOutputStream(String fileName,
//                                                HttpServletResponse response) throws Exception {
//        try {
//            fileName = URLEncoder.encode(fileName, "UTF-8");
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf8");
//            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
//            response.setHeader("Pragma", "public");
//            response.setHeader("Cache-Control", "no-store");
//            response.addHeader("Cache-Control", "max-age=0");
//            return response.getOutputStream();
//        } catch (IOException e) {
//            throw new Exception("导出excel表格失败!", e);
//        }
//    }
//}
//
