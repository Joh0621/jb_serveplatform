import com.alibaba.fastjson.JSONObject;
import com.bonc.jibei.RunApplication;
import com.bonc.jibei.service.ReportService;
import freemarker.template.TemplateException;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * jb_serveplatform
 *
 * @author renguangli
 * @date 2022/5/5 10:00
 */
@SpringBootTest(classes = RunApplication.class)
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Resource
    private ReportService reportService;

    @org.junit.Test
    public void testReport() throws TemplateException, IOException {
        JSONObject params = new JSONObject();
        params.put("reportId", 3);
        params.put("stationId", 934);
        params.put("typeId", 1);
        params.put("startTime", "2022-01-01");
        params.put("endTime", "2022-04-01");
//        String modelFileName = "1.ftl";
//        String reportFileName = "风电场运行性能评估分析报告模板V1版本.docx";
        reportService.generate(params);
    }

}
