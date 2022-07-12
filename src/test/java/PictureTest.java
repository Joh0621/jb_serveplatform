import cn.hutool.core.convert.Convert;
import cn.hutool.extra.template.TemplateException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.jibei.util.EchartsToPicUtil;
import com.bonc.jibei.util.PoiTLUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ：wjx.
 * @version ：
 * @date ：Created at 19:28 2022/7/5
 */
public class PictureTest {

    @Test
    public void test() {
        String json = "{\n" +
                "            \"type\": \"radar\",\n" +
                "            \"name\": \"deviceRadar\",\n" +
                "            \"value\": {\n" +
                "              \"xData\": [\n" +
                "                121.42,\n" +
                "                100,\n" +
                "                100,\n" +
                "                100,\n" +
                "                100,\n" +
                "                100\n" +
                "              ],\n" +
                "              \"yData\": [\n" +
                "                121.42,\n" +
                "                50,\n" +
                "                97.07234706,\n" +
                "                63.00859237,\n" +
                "                100,\n" +
                "                100\n" +
                "              ],\n" +
                "              \"yName\": [\n" +
                "                \"fg\",\n" +
                "                \"功率曲线评价\",\n" +
                "                \"发电时长\",\n" +
                "                \"发电量得分\",\n" +
                "                \"可靠性得分\",\n" +
                "                \"平均可利用率\"\n" +
                "              ]\n" +
                "            }\n" +
                "          }";
        JSONObject value = JSON.parseObject(json).getJSONObject("value");
        System.out.println(value);
        String[] xData = Convert.toStrArray(value.getJSONArray("xData"));
        String[] yNames = Convert.toStrArray(value.getJSONArray("yName"));
        Double[] yData =  Convert.toDoubleArray(value.getJSONArray("yData"));
        String[] titles = {};
        String path = EchartsToPicUtil.echartRadar(true, titles, xData, yData, yNames);
        System.out.println(path);
    }
}
