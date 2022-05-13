import com.deepoove.poi.XWPFTemplate;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/5/5 10:00
 */
public class PoiTLTest {

    @Test
    public void testPoiTl() throws IOException {
        String templatePath = "docs/XXX风电场运行性能评估分析报告模板V1版本-poi-tl.docx";
        String outPath = "docs/OUT_XXX风电场运行性能评估分析报告模板V1版本-poi-tl.docx";

        Map<String, Object> data = new HashMap<>();
        data.put("stationName", "迎风铃风电场");

        XWPFTemplate.compile(templatePath)
                .render(data)
                .writeToFile(outPath);
    }

}
