import com.bonc.jibei.util.PoiTLUtils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.render.RenderContext;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/5/5 10:00
 */
public class PoiTLTest {

    @Test
    public void testPoiTl() throws IOException {
        ConfigureBuilder builder = Configure.builder()
                .useSpringEL(true); // 开启 springEL 表达式
        String templatePath = "docs/附件-poi-tl.docx";
        String outPath = "docs/OUT_附件-poi-tl.docx";


        List<HashMap<String, Object>> table = Arrays.asList(
                new HashMap<String, Object>() {{
                   put("name", "小明");
                   put("age", 18);
                   put("gender", "男");
               }}, new HashMap<String, Object>() {{
                   put("name", "小红");
                   put("age", 18);
                   put("gender", "女");
               }}
        );
        bindLoopRowTablePolicy(builder, "table");

        List<HashMap<String, Object>> list = Arrays.asList(new HashMap<String, Object>() {{
            put("img", PoiTLUtils.picData("D:\\data\\img\\000cee3b.png"));
            put("stationName", "大唐");
            put("table", table);
        }},new HashMap<String, Object>() {{
            put("img", PoiTLUtils.picData("D:\\data\\img\\000cee3b.png"));
            put("stationName", "迎风");
            put("table", table);
        }});
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);


        List<HashMap<String, Object>> table2 = Arrays.asList(
                new HashMap<String, Object>() {{
                    put("name", "小明1");
                    put("age", 18);
                    put("gender", "男1");
                }}, new HashMap<String, Object>() {{
                    put("name", "小1红");
                    put("age", 181);
                    put("gender", "女1");
                }}
        );
        data.put("table", table2);
        XWPFTemplate.compile(templatePath, builder.build())
                .render(data)
                .writeToFile(outPath);
    }

    /**
     * 绑定表格行循环插件
     * @param builder builder
     * @param tagName tagName
     */
    private static void bindLoopRowTablePolicy(ConfigureBuilder builder, String tagName){
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        builder.bind(tagName, policy);
    }
}
