import cn.hutool.core.convert.Convert;
import cn.hutool.extra.template.TemplateException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.controller.DeviceController;
import com.bonc.jibei.util.EchartsToPicUtil;
import com.bonc.jibei.util.PoiTLUtils;
import com.bonc.jibei.vo.DeviceModelVo;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ：wjx.
 * @version ：
 * @date ：Created at 19:28 2022/7/5
 */
public class PictureTest {
    DeviceController deviceController;
    @Test
    public void  cltest() {
        DeviceModelVo deviceModelVo = new DeviceModelVo();
        deviceModelVo.setDeviceCompany("8866");
        deviceModelVo.setDeviceType("风电机组");
        deviceModelVo.setModelCode("8864");
        Result deviceInfo = deviceController.getDeviceInfo(deviceModelVo);
        System.out.println(deviceInfo);
//        getDeviceInfo// 此处是我本地项目的mapper，是为了获取省份数据
//        List<AreaModel> result = new ArrayList<>();
//        areas.stream().forEach(area -> {
//            AreaModel areaModel = new AreaModel();
//            BeanUtils.copyProperties(area,areaModel);
//            result.add(areaModel);
//        });
//        IntStream.range(0, result.size()).forEach(i -> {
//            result.get(i).setNo(i+1);
//        });
//        try {
//            Map<String,Object> param = new HashMap<>();
//            param.put("title","省份数据");
//            param.put("list",result);
//            TemplateExcelUtils.downLoadExcel("省份数据","province.xls",param,response);
//        } catch (Exception e) {
//            throw new ServiceException(ResultCode.EXPORT_ERROR);
        return ;
        }
    }

