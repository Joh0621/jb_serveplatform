import cn.hutool.extra.template.TemplateException;
import com.alibaba.fastjson.JSONObject;
import com.bonc.jibei.RunApplication;
import com.bonc.jibei.service.ReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
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
        reportService.generate(params);
    }

}
