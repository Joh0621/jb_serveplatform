package com.bonc.jibei.util;

import com.github.abel533.echarts.Radar;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.RadarData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.RadarSeries;
import com.github.abel533.echarts.style.TextStyle;
import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;
/**
 * eachrts  根据数据形成后台出图
 */
@Component
public class EchartsToPicUtil {

    private static String pngPathV;
    private static String OSTypeV;

    private static Log log = LogFactory.getLog(EchartsToPicUtil.class);

    @Value("${spring.cfg.pngPath}")
    private String  pngPath;
    @Value("${spring.cfg.OSType}")
    private String  OSType;

    @PostConstruct
    public void getMngPath(){
        pngPathV=pngPath;
    }
    @PostConstruct
    public void getOSType(){
        OSTypeV=OSType;
    }

    /**
     * 柱状图
     * @param isHorizontal  是否水平放置
     * @param title 标题
     * @param xData x轴数据
     * @param yData y轴数据
     * @return
     */
    public static String echartBar(boolean isHorizontal,String title,String[] xData,double[] yData) {
       /**
		String[] colors = { "rgb(2,111,230)", "rgb(186,73,46)",
				"rgb(78,154,97)", "rgb(2,111,230)", "rgb(186,73,46)",
				"rgb(78,154,97)" };
        **/
        GsonOption option = new GsonOption();
        option.title(title); // 标题
        // 工具栏
        option.toolbox().show(true).feature(Tool.mark, // 辅助线
                Tool.dataView, // 数据视图
                new MagicType(Magic.line, Magic.bar),// 线图、柱状图切换
                Tool.restore,// 还原
                Tool.saveAsImage);// 保存为图片

        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");// 显示工具提示,设置提示格式
        option.legend(title);// 图例
        Bar bar = new Bar(title);// 图类别(柱状图)
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(xData);// 轴数据类别
        // 循环数据
        for (int i = 0; i < xData.length; i++) {
            double data = yData[i];
//			String color = colors[i];
            // 类目对应的柱状图
            HashMap<String, Object> map = new HashMap<String, Object>(2);
            map.put("value", data);
//			map.put("itemStyle",new ItemStyle().normal(new Normal().color(color)));
//
            bar.data(map);
        }
        if (isHorizontal) {// 横轴为类别、纵轴为值
            option.xAxis(category);// x轴
            option.yAxis(new ValueAxis());// y轴
        } else {// 横轴为值、纵轴为类别
            option.xAxis(new ValueAxis());// x轴
            option.yAxis(category);// y轴
        }
        option.series(bar);
        return generateEChart(new Gson().toJson(option));
    }
    /**
     * 折线图
     * @param isHorizontal 是否水平放置
     *@param xData x轴数据
     *@param yData y轴数据
     */
    public static String echartLine(boolean isHorizontal,String title,String[] yBarName,String[] xData ,double[][] yData) {
       // yBarName = new String[]{"邮件营销", "联盟广告", "视频广告"};
        /**
        yData = new double[][]{{120, 132, 101, 134, 90, 230, 210},
                {220, 182, 191, 234, 290, 330, 310},
                {150, 232, 201, 154, 190, 330, 410}};
         **/
        //xData=new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
       // title = "广告数据";
        GsonOption option = new GsonOption();
        option.title().text(title).subtext("虚构").x("left");// 大标题、小标题、位置
        // 提示工具
        option.tooltip().trigger(Trigger.axis);// 在轴上触发提示数据
        // 工具栏
        option.toolbox().show(true).feature(Tool.saveAsImage);// 显示保存为图片
        option.legend(yBarName);// 图例
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(xData);
        category.boundaryGap(false);// 起始和结束两端空白策略
        // 循环数据
        for (int i = 0; i < yBarName.length; i++) {
            Line line = new Line();// 三条线，三个对象
            String type = yBarName[i];
            line.name(type).stack("总量");
            for (int j = 0; j < yData[i].length; j++) {
                line.data(yData[i][j]);
            }
            option.series(line);
        }
        if (isHorizontal) {// 横轴为类别、纵轴为值
            option.xAxis(category);// x轴
            option.yAxis(new ValueAxis());// y轴
        } else {// 横轴为值、纵轴为类别
            option.xAxis(new ValueAxis());// x轴
            option.yAxis(category);// y轴
        }
        return generateEChart(new Gson().toJson(option));
    }
    /**
     * 柱状|线状图
     * @param isHorizontal 是否水平放置,title:表名；type：图形标识：Bar 是柱状，否则是线状
     */
    public static String echartBarand(boolean isHorizontal,String title,String[] xData,double[][] yData,String [] type ,String[] yBarName){

        GsonOption option = new GsonOption();
        option.title(title); // 标题
        // 工具栏
        option.toolbox().show(true).feature(Tool.mark, // 辅助线
                Tool.dataView, // 数据视图
                new MagicType(Magic.line, Magic.bar),// 线图、柱状图切换
                Tool.restore,// 还原
                Tool.saveAsImage);// 保存为图片
        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");// 显示工具提示,设置提示格式
//		option.legend("Evaporation", "Precipitation", "Temperature");
        Bar bar = new Bar();// 图类别(柱状图)
        Line line = new Line();
        CategoryAxis category = new CategoryAxis();// 轴分类、
        // 循环数据
        for (int i = 0; i < yBarName.length; i++) {
            // 类目对应的柱状图
            //根据是柱状或者线状，构造对象
            if("Bar".equals(type[i])){
                bar = new Bar(yBarName[i]);
            }
            else {
                line = new Line(yBarName[i]);
            }
            for (int j = 0; j < yData[i].length; j++) {
                if("Bar".equals(type[i])){
                    bar.data(yData[i][j]);
                }else {
                    line.data(yData[i][j]);
                }
            }
            if("Bar".equals(type[i])){
                option.series(bar);
            }else {
                option.series(line);
            }
        }
        option.xAxis(new CategoryAxis().data(xData));
        option.yAxis(new ValueAxis().max("250").min(0));
       // option.yAxis(new ValueAxis().max("25").min(0));
        if (isHorizontal) {// 横轴为类别、纵轴为值
            option.xAxis(category);// x轴
            option.yAxis(new ValueAxis());// y轴
        } else {// 横轴为值、纵轴为类别
            option.xAxis(new ValueAxis());// x轴
            option.yAxis(category);// y轴
        }
        return generateEChart(new Gson().toJson(option));
       // return new Gson().toJson(option);
    }

    /**
     * 饼状图
     * * @param isHorizontal 是否水平放置
     * 	 *@param names 名称
     * 	 *@param datas 数据值
     * 	 *@param title 图表名
     * @return
     */
    public static String echartPie(boolean isHorizontal,String title,String[] names,double[] datas){
        //names = new String[]{"Search Engine", "Direct", "Email", "Union Ad", "Video Ads"};
       // datas = new double[]{1048, 735, 580, 484, 300};
        List<Map> list= Lists.newArrayList();
        for (int i = 0; i < names.length; i++) {
            Map data = new HashMap<>();//通过map存放要填充的数据
            data.put("name",names[i]);
            data.put("value",datas[i]);
            list.add(data);
        }
       // title = "标题";
        GsonOption option = new GsonOption();
        option.title(title); // 标题
        Pie bar = new Pie(title);// 图类别(柱状图)
        // 循环数据
        for (int i = 0; i < names.length; i++) {
            bar.data(list.get(i));
        }
        bar.radius("50%");
        option.series(bar);
        return generateEChart(new Gson().toJson(option));
    }

    /**
     * 多组柱状图
     * * @param isHorizontal 是否水平放置
     * 	 *@param xData x轴数据
     * 	 *@param yBarName y轴显示的柱状数据名称
     * 	 *@param yData y轴显示的多个柱状图数据
     * 	 *@param title[] 标题 包括主标题和子标题
     * @return
     */
    public static String echartBarGroup(Boolean isHorizontal,String[] title,String[] yBarName,String[] xData ,double[][] yData){
        EnhancedOption option = new EnhancedOption();
        if (title.length>1) {
            Title t=option.title().text(title[0]);
            for (int i=1;i<title.length;i++){
                t.subtext(title[i]);
            }
        }else if (title.length>0) {
            option.title().text(title[0]);
        }
        option.tooltip().trigger(Trigger.axis);
        option.legend(yBarName);
        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.calculable(true);
        option.xAxis(new CategoryAxis().data(xData));
        option.yAxis(new ValueAxis());

        for (int i = 0; i < yBarName.length; i++) {
            Bar bar = new Bar(yBarName[i]);
            for (int j = 0; j < yData[i].length; j++) {
                bar.data(yData[i][j]);
            }
            option.series(bar);
        }
        return generateEChart(new Gson().toJson(option));
    }
    /**
     * 堆叠图
     *  * * @param isHorizontal 是否水平放置
     * 	 * 	 *@param xData x轴数据
     * 	 * 	 *@param yBarName y轴显示的柱状数据名称
     * 	 * 	 *@param yData y轴显示的多个柱状图数据
     * @return
     */
    public static String echartStackedBare(Boolean isHorizontal,String title,String[] xData ,String[] yName,double[][] yData){
        EnhancedOption option = new EnhancedOption();
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
        option.legend(yName);
        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.calculable(true);
        option.yAxis(  new ValueAxis());
        option.xAxis(new CategoryAxis().data(xData));

        for (int i = 0; i < yName.length; i++) {
            Bar bar = new Bar(yName[i]);
            bar.stack("总量");
            bar.itemStyle().normal().label().show(true).position("insideRight");
            for (int j = 0; j < yData[i].length; j++) {
                bar.data(yData[i][j]);
            }
            option.series(bar);
        }
        return generateEChart(new Gson().toJson(option));
    }
    /**
     * 雷达图
     *  * * @param isHorizontal 是否水平放置
     * 	 * 	 *@param xData x轴数据
     * 	 * 	 *@param yBarName y轴显示的柱状数据名称
     * 	 * 	 *@param yData y轴显示的多个柱状图数据
     * 	 *@param title 主标题 和子标题
     * @return
     */
    public static String echartRadar(Boolean isHorizontal,String title[],String[] xData,double[][] yData ,String[] yName){
        //此图可理解为平铺的柱状图，x轴为统计的类别，y轴为具体数据和名称
        EnhancedOption option = new EnhancedOption();
        if (title!=null && title.length>1){
            Title t=option.title().text(title[0]);
            for (int i=1;i<title.length;i++){
                t.subtext(title[i]);
            }
            option.title().text(title[0]).subtext(title[1]);
        }
        else if(title!=null && title.length>0){
            option.title().text(title[0]);
        }
        option.legend(MapUtils.putAll(new HashMap(),yName).values());
        //设置 Radar
        Radar radar = new Radar();
        radar.name(new Radar.Name().textStyle(new TextStyle().color("#fff").backgroundColor("#999").borderRadius(3).padding(new Integer[]{3, 5})));
        RadarSeries radar1 = new RadarSeries("预算 vs 开销（Budget vs spending）");
        for (int i = 0; i < yName.length; i++) {

            RadarData radarData1 = new RadarData(yName[i], yData[i]);
            radar1.data(radarData1);
            for (int j = 0; j < xData.length; j++) {
                //此处的边界最大次是否需要作为入参，可讨论
                double max = yData[0][j];
                max= yData[i][j]>max?yData[i][j]:max;
                if (i==yName.length-1) {
                    radar.indicator(new Radar.Indicator().name(xData[j]).max(max*1.2));
                }
            }
        }
        option.radar(radar);
        option.series(radar1);
        String gson=new Gson().toJson(option);
        return generateEChart(new Gson().toJson(option));
    }
    @SuppressWarnings("finally")
    public static String generateEChart(String options) {
        //String OSTypeV="windows";
        //String pngPathV="d:/test/png/png/";
        String rootPath="";
        if ("windows".equals(OSTypeV)) {
            rootPath = EchartsToPicUtil.class.getResource("/").toString().replace("file:/", "");
        }
        else {
            rootPath = EchartsToPicUtil.class.getResource("/").toString().replace("file:", "");
        }
        String echartJsPath = rootPath + "echarts-convert" + File.separator + "echarts-convert1.js";
        String OSPath = switchOS(OSTypeV);
        String dataPath = writeFile(options, pngPathV);
        String fileName = UUID.randomUUID().toString().substring(0, 8) + ".png";
        String path = pngPathV + fileName;
        try {
            File file = new File(path); // 文件路径（路径+文件名）
            if (!file.exists()) { // 文件不存在则创建文件，先创建目录
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            String cmd = rootPath + "phantomjs" + File.separator + OSPath + File.separator + "phantomjs " + echartJsPath + " -infile " + dataPath
                    + " -outfile " + path;
            log.info("shell " + cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                log.info(line);
            }
            input.close();

            File jsonFile = new File(dataPath);
            if (jsonFile.exists()) {
                jsonFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //return path;
            return PictureUtils.getImageBase(path);
        }
    }
    public static String writeFile(String options, String tmpPath) {
        String dataPath = tmpPath
                + UUID.randomUUID().toString().substring(0, 8) + ".json";
        try {
            /* 写入Txt文件 */
            File writename = new File(dataPath); // 相对路径，如果没有则要建立一个新的output.txt文件
            if (!writename.exists()) { // 文件不存在则创建文件，先创建目录
                File dir = new File(writename.getParent());
                dir.mkdirs();
                writename.createNewFile(); // 创建新文件
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(options); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPath;
    }

    private static String switchOS(String OSType) {
        if ("linux".equals(OSType)) {
            return "linux-x86";
        } else if ("windows".equals(OSType)) {
            return "windows";
        } else if ("mac".equals(OSType)) {
            return "mac";
        } else {
            return "windows";
        }
    }
    public static void main(String[] args) {
        //雷达图
        /*
        String[] title={"基础雷达图","基础下级雷达图"};
        String[] yName=new String[]{"预算分配","实际开销"};
        String[] xData=new String[]{"销售","管理","信息技术","客服","研发","市场"};
        double[][] yData=new double[][]{{4300, 10000, 28000, 35000, 50000, 19000},{5000, 14000, 28000, 31000, 42000, 21000}};
        echartRadar(true,title,xData,yData ,yName);
        */
        //echartRadar(true,null,null,null ,null);
        //堆叠图
       /*
        String[] xData=new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        String[] yName=new String[]{"直接访问", "邮件营销", "联盟广告", "视频广告", "搜索引擎"};

        double[][] yData=new double[][]{
         {320, 302, 301, 334, 390, 330, 320},
         {320, 302, 301, 334, 390, 330, 320},
         {120, 132, 101, 134, 90, 230, 210},
         {150, 212, 201, 154, 190, 330, 410},
         {820, 832, 901, 934, 1290, 1330, 1320},
         };
        echartStackedBare(true, null, xData ,yName,yData);
        */
       //多组柱状图
        /*
        String[] title={"某地区蒸发量和降水量","纯属虚构"};
        String[] yBarName=new String[]{"蒸发量", "降水量","消耗量"};
        String[] xData=new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};

        double[][] yData = new double[][]{{2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3},
         {2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3},
         {11.6, 15.9, 19.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3}};
        echartBarGroup(true,title,yBarName,xData ,yData);
        */
        //柱状图
        /*
        String title="test测试";
        String[] xData = new String[]{"广州", "深圳", "珠海", "汕头", "韶关", "佛山"};
        double[] yData = new double[]{6030, 7800, 5200, 3444, 2666, 5708};
        echartBar(true,title,xData,yData);
        */
        //柱状|线状图
        /*
        double[][] yData = new double[][]{{2.0, 2.2, 3.3}, {2.0, 4.9, 7.0}, {12.0, 4.9, 17.0}};
        String[] xData =new String[]{"Mon", "Tue", "Wed"};

        int[] datas = {6030, 7800, 5200, 3444, 2666, 5708};
        String[] yBarName = new String[]{"Evaporation", "Precipitation", "Temperature"};
        String []  type = new String[]{"Bar", "Line","Line"};
        String title = "地市数据";
        echartBarand(true, title, xData, yData,type , yBarName);
        */
    }
}
