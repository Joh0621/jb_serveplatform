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
        reportService.generate(3);
    }

}
