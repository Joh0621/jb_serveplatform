import com.alibaba.fastjson.JSONObject;
import com.bonc.jibei.RunApplication;
import com.bonc.jibei.service.ReportPoiTlService;
import com.bonc.jibei.service.ReportService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    private ReportPoiTlService reportPoiTlService;

    @Test
    public void testReport() throws TemplateException, IOException, ClassNotFoundException {
        JSONObject params = new JSONObject();
        params.put("modelId", 1);
//        params.put("reportId", 1);
        params.put("stationId", "610");
        params.put("typeId", "1");
        params.put("startTime", "2022-01-01");
        params.put("endTime", "2022-04-01");
//        String modelFileName = "1.ftl";
//        String reportFileName = "风电场运行性能评估分析报告模板V1版本.docx";
        reportPoiTlService.generate(params);
    }


    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setDirectoryForTemplateLoading(new File("d:/data/template"));
        Template template = configuration.getTemplate("1.ftl", StandardCharsets.UTF_8.name());
    }

}
