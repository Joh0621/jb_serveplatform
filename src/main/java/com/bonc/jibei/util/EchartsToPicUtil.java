package com.bonc.jibei.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.google.gson.Gson;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * eachrts  根据数据形成后台出图
 */
public class EchartsToPicUtil {

    private static Log log = LogFactory.getLog(EchartsToPicUtil.class);

    @SuppressWarnings("finally")
    public static String generateEChart(String options, String tmpPath, String OSType, String rootPath) {
//        String rootPath = EchartsToPicUtil.class.getResource("/").toString().replace("file:/", "");
        String echartJsPath = rootPath + "echarts-convert" + File.separator + "echarts-convert1.js";
        String OSPath = switchOS(OSType);
        String dataPath = writeFile(options, tmpPath);
        String fileName = UUID.randomUUID().toString().substring(0, 8) + ".png";
        String path = tmpPath + fileName;
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
            return path;
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

    /**
     * 折线图
     *
     * @param isHorizontal 是否水平放置
     */
    public static String echartLine(boolean isHorizontal) {
        String[] types = {"邮件营销", "联盟广告", "视频广告"};
        int[][] datas = {{120, 132, 101, 134, 90, 230, 210},
                {220, 182, 191, 234, 290, 330, 310},
                {150, 232, 201, 154, 190, 330, 410}};
        String title = "广告数据";

        GsonOption option = new GsonOption();

        option.title().text(title).subtext("虚构").x("left");// 大标题、小标题、位置

        // 提示工具
        option.tooltip().trigger(Trigger.axis);// 在轴上触发提示数据
        // 工具栏
        option.toolbox().show(true).feature(Tool.saveAsImage);// 显示保存为图片

        option.legend(types);// 图例

        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data("周一", "周二", "周三", "周四", "周五", "周六", "周日");
        category.boundaryGap(false);// 起始和结束两端空白策略

        // 循环数据
        for (int i = 0; i < types.length; i++) {
            Line line = new Line();// 三条线，三个对象
            String type = types[i];
            line.name(type).stack("总量");
            for (int j = 0; j < datas[i].length; j++) {
                line.data(datas[i][j]);
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

        return new Gson().toJson(option);
    }

    /**
     * 柱状图
     *
     * @param isHorizontal 是否水平放置
     */
    public static String echartBar(boolean isHorizontal) {
        String[] citis = {"广州", "深圳", "珠海", "汕头", "韶关", "佛山"};
        int[] datas = {6030, 7800, 5200, 3444, 2666, 5708};
        String[] colors = {"rgb(2,111,230)", "rgb(186,73,46)",
                "rgb(78,154,97)", "rgb(2,111,230)", "rgb(186,73,46)",
                "rgb(78,154,97)"};
        String title = "地市数据";

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
        category.data(citis);// 轴数据类别
        // 循环数据
        for (int i = 0; i < citis.length; i++) {
            int data = datas[i];
            String color = colors[i];
            // 类目对应的柱状图
            HashMap<String, Object> map = new HashMap<String, Object>(2);
            map.put("value", data);
            map.put("itemStyle", new ItemStyle().normal(new Normal().color(color)));
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
        return new Gson().toJson(option);
    }



	public static void  testt() {
		HackLoopTableRenderPolicy policy = new HackLoopTableRenderPolicy();
		Configure config = Configure.builder().bind("list", policy).build();
		List<Map> list= Lists.newArrayList();
		for (int i = 0; i < 5; i++) {
			Map data = new HashMap<>();//通过map存放要填充的数据
			data.put("year","201"+i);
			data.put("num",33+i);
			data.put("num1",33+i);
			try {
				data.put("encharts1", new PictureRenderData(500, 300, PictureType.PNG, BytePictureUtils.toByteArray(new FileInputStream("D:\\echartss\\d279ee2f.png"))));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			list.add(data);
		}
		Map data = new HashMap<>();//通过map存放要填充的数据
		data.put("list",list);

		data.put("title", "业绩报表");
		data.put("userName", "JUVENILESS");
		data.put("time", "2018-03-24");
		data.put("sex", "男");

		data.put("saignuser", "男男男");
		data.put("date", "2022-05-01");

		try {
			data.put("encharts", new PictureRenderData(500, 300, PictureType.PNG, BytePictureUtils.toByteArray(new FileInputStream("D:\\echartss\\d279ee2f.png"))));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XWPFTemplate template = XWPFTemplate.compile("D:\\test\\ccc.docx",config).render(data);//调用模板，填充数据
		try {
			FileOutputStream out = new FileOutputStream("d:\\test\\test111.docx");//要导出的文件名
			template.write(out);
			out.flush();
			out.close();
			template.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		EchartsToPicUtil.testt();


	}
}
