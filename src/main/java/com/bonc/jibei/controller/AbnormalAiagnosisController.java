package com.bonc.jibei.controller;

import com.bonc.jibei.api.Result;
import com.bonc.jibei.entity.*;
import com.bonc.jibei.mapper.AbnormalAiagnosisMapper;
import com.bonc.jibei.vo.RadiationDoseDistributedVo;
import com.bonc.jibei.vo.UseOfHoursVo;
import com.bonc.jibei.vo.powerComponentsVo;
import com.bonc.jibei.vo.powerInverterStatusVo;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("abnormalAiagnosis")
@RestController
//场站异常诊断
public class AbnormalAiagnosisController {
    @Resource
    private AbnormalAiagnosisMapper abnormalAiagnosisMapper;

    /**
     * 发电单元发电性能与稳定性整体评价
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerUnitEvaluation")
    @ResponseBody
    public Result powerUnitEvaluation(String yearMonth,String stationId) {
        return Result.ok(abnormalAiagnosisMapper.powerUnitEvaluation(yearMonth, stationId));
    }


    /**
     * 发电单元功率曲线
     * @param yearMonth
     * @param stationId
     * @param powerUnit  发电单元
     * @return
     */
    @RequestMapping("powerUnitPower")
    @ResponseBody
    public Result powerUnitPower(String yearMonth,String stationId,String powerUnit) {
        List<String> strings = Arrays.asList(powerUnit.split(","));
       List< Map<String, Object>> resultMap = new ArrayList<>();
        List<UseOfHoursVo> powerUnitPower = abnormalAiagnosisMapper.powerUnitPower(yearMonth, stationId, strings);
        Map<String, List<UseOfHoursVo>> map = powerUnitPower.stream().collect(
                Collectors.groupingBy(
                        model -> model.getYData1()
                ));
        Map<String, Object> bData = new HashMap<>();
        for (Map.Entry<String, List<UseOfHoursVo>> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            ArrayList<Object> xList = new ArrayList<>();
            ArrayList<Object> yList = new ArrayList<>();
            Map<String, Object> mapData = new HashMap<>();
            for ( UseOfHoursVo value : entry.getValue()) {
                    xList.add(value.getXData());
                    yList.add(value.getYData());
            }
            mapData.put("xData",xList);
            mapData.put("yData",yList);
            bData.put(entry.getKey(),mapData);
        }
        return Result.ok(bData);
    }


    /**
     * 逆变器发电异常监测
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerinverterError")
    @ResponseBody
    public Result powerinverterError(String yearMonth,String stationId) {
        return Result.ok(abnormalAiagnosisMapper.powerInverterErrorPosition(yearMonth, stationId));
    }

    /**
     * 逆变器发电异常监测
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerInverterErrorStatistics")
    @ResponseBody
    public Result powerInverterErrorStatistics(String yearMonth,String stationId) {
        powerInverterStatusVo vo = abnormalAiagnosisMapper.powerInverterErrorStatistics(yearMonth, stationId);
        return Result.ok(vo);
    }
    /**
     * 逆变器转换效率
     * @param yearMonth
     * @param inverter
     * @return
     */
    @RequestMapping("powerInverterconversionEfficiency")
    @ResponseBody
    public Result powerInverterconversionEfficiency(String yearMonth,String stationId,String inverter) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerInverterconversionEfficiency(yearMonth, stationId, inverter);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 逆变器发电量排名TOP10
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerInverterPower")
    @ResponseBody
    public Result powerInverterPower(String yearMonth,String stationId) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerInverterPower(yearMonth, stationId);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 逆变器故障次数排名TOP10
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerInverterFaultCnt")
    @ResponseBody
    public Result powerInverterFaultCnt(String yearMonth,String stationId) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerInverterFaultCnt(yearMonth, stationId);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 逆变器健康预警监测
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerInverterWarning")
    @ResponseBody
    public Result powerInverterWarning(String yearMonth,String stationId) {
        return Result.ok(abnormalAiagnosisMapper.powerInverterWarning(yearMonth, stationId));
    }

    /**
     * 逆变器健康预警监测
     * @param inverter
     * @param warningTime
     * @return
     */
    @RequestMapping("powerInverterWarningDetail")
    @ResponseBody
    public Result powerInverterWarningDetail(String inverter,String warningTime,String warningType,String warningDesc) {
        return Result.ok(abnormalAiagnosisMapper.powerInverterWarningDetail(inverter, warningTime,warningType,warningDesc));
    }

    /**
     * 发电异常组串统计
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsStringStatistics")
    @ResponseBody
    public Result powerComponentsStringStatistics(String yearMonth,String stationId) {
        List<powerComponentsString> powerComponentsStrings = abnormalAiagnosisMapper.powerComponentsStringStatistics(yearMonth, stationId);
        ArrayList<Object> xList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (powerComponentsString vo: powerComponentsStrings){
            if ("0".equals(vo.getStatus())) {
                map.put("异常", vo.getComponentsString());
            } else {
                map.put("正常", vo.getComponentsString());
            }


        }
        return Result.ok(map);
    }

    /**
     * 发电异常组串数  TOP5
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsStringTop5")
    @ResponseBody
    public Result powerComponentsStringTop5(String yearMonth,String stationId) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerComponentsStringTop5(yearMonth, stationId);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 发电异常组串列表
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsStringList")
    @ResponseBody
    public Result powerComponentsStringList(String yearMonth,String stationId) {
        return Result.ok(abnormalAiagnosisMapper.powerComponentsStringList(yearMonth, stationId));
    }


    /**
     * 发电异常组串定位
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsStringLocation")
    @ResponseBody
    public Result powerComponentsStringLocation(String yearMonth,String stationId) {
        List<powerComponentsString> powerComponentsStrings = abnormalAiagnosisMapper.powerComponentsStringLocation(yearMonth, stationId);
        Map<String, List<powerComponentsString>> map = powerComponentsStrings.stream().collect(
                Collectors.groupingBy(
                        model -> model.getPowerUnit()
                ));
        Map<String,powerComponentsStringRe> mapRe=new HashMap<>();

        for (Map.Entry<String, List<powerComponentsString>> entry : map.entrySet()) {
            powerComponentsStringRe powerComponentsStringRe = new powerComponentsStringRe();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            Integer xMax=0;
            Integer yMax=0;
            for ( powerComponentsString value : entry.getValue()) {
                String[] split = value.getComponentsStringLocation().split("-");
                xMax=Integer.valueOf( split[0])>xMax?Integer.valueOf( split[0]):xMax;
                yMax=Integer.valueOf( split[1])>yMax?Integer.valueOf( split[1]):yMax;
            }
            powerComponentsStringRe.setLlocation(xMax+"-"+yMax);
            powerComponentsStringRe.setPowerComponentsString(entry.getValue());
            mapRe.put(entry.getKey(),powerComponentsStringRe);
        }
        return Result.ok(mapRe);
    }


    /**
     * 组串故障详情
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsStringDetail")
    @ResponseBody
    public Result powerComponentsStringDetail(String yearMonth,String id) {
        return Result.ok(abnormalAiagnosisMapper.powerComponentsStringDetail(yearMonth,id));
    }

    /**
     * 组件故障类型分布
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsErrorDistributed")
    @ResponseBody
    public Result powerComponentsErrorDistributed(String yearMonth,String stationId) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerComponentsErrorDistributed(yearMonth, stationId);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     *
     * 组件故障统计
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsErrorList")
    @ResponseBody
    public Result powerComponentsErrorList(String yearMonth,String stationId) {
        List<powerComponentsVo> useOfHoursVos = abnormalAiagnosisMapper.powerComponentsErrorList(yearMonth, stationId);
        ArrayList<Object> xList = new ArrayList<>();
        double sum = useOfHoursVos.stream().mapToDouble(powerComponentsVo::getComponentsErrotCnt).sum();
        for (powerComponentsVo vo: useOfHoursVos){
           vo.setComponentsErrotRate(vo.getComponentsErrotCnt()/sum);
        }
        return Result.ok(useOfHoursVos);
    }

    /**
     * 发电异常组件定位
     * @param yearMonth
     * @param stationId
     * @return
     */
    @RequestMapping("powerComponentsLocation")
    @ResponseBody
    public Result powerComponentsLocation(String yearMonth,String stationId) {
        List<powerComponents> powerComponents = abnormalAiagnosisMapper.powerComponentsLocation(yearMonth, stationId);
        Map<String, List<powerComponents>> map = powerComponents.stream().collect(
                Collectors.groupingBy(
                        model -> model.getPowerUnit()
                ));
        Map<String,powerComponentsRe> mapRe=new HashMap<>();

        for (Map.Entry<String, List<powerComponents>> entry : map.entrySet()) {
            powerComponentsRe powerComponentsStringRe = new powerComponentsRe();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            Integer xMax=0;
            Integer yMax=0;
            for ( powerComponents value : entry.getValue()) {
                String[] split = value.getComponentsLocation().split("-");
                xMax=Integer.valueOf( split[0])>xMax?Integer.valueOf( split[0]):xMax;
                yMax=Integer.valueOf( split[1])>yMax?Integer.valueOf( split[1]):yMax;
            }
            powerComponentsStringRe.setLlocation(xMax+"-"+yMax);
            powerComponentsStringRe.setPowerComponents(entry.getValue());
            mapRe.put(entry.getKey(),powerComponentsStringRe);
        }
        return Result.ok(mapRe);
    }

    /**
     * 组件故障详情
     * @param yearMonth
     * @param id
     * @return
     */
    @RequestMapping("powerComponentsDetail")
    @ResponseBody
    public Result powerComponentsDetail(String yearMonth,String id) {
        return Result.ok(abnormalAiagnosisMapper.powerComponentsDetail(yearMonth, id));
    }



    /**
     * 发电效能RP分析
     * @param stationId
     * @param
     * @return
     */
    @RequestMapping("powerComponentsPr")
    @ResponseBody
    public Result powerComponentsPr(String stationId ) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.powerComponentsPr( stationId);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 损失电量分析
     * @param stationId
     * @param
     * @return
     */
    @RequestMapping("lostPowerAnalyze")
    @ResponseBody
    public Result lostPowerAnalyze(String stationId ) {
        List<UseOfHoursVo> useOfHoursVos = abnormalAiagnosisMapper.lostPowerAnalyze( stationId);
        ArrayList<Object> xList = new ArrayList<>();
        for (UseOfHoursVo vo: useOfHoursVos){
            Map<String, Object> map = new HashMap<>();
            map.put("yData",vo.getYData());
            map.put("yData1",vo.getYData1());
            map.put("xData",vo.getXData());
            xList.add(map);
        }
        return Result.ok(xList);
    }

    /**
     * 清洗经济性计算
     * @param stationId
     * @param
     * @return
     */
    @RequestMapping("cleaningEconomicCalculation")
    @ResponseBody
    public Result cleaningEconomicCalculation(String stationId ,Double price) {
        String s = abnormalAiagnosisMapper.cleaningEconomicCalculation(stationId);
       if (price!=null&&s!=null){
           return Result.ok(price*Double.valueOf(s));
       }else {
           return Result.ok();
       }

    }

    @SneakyThrows
    @ApiOperation(value = "返回图片流")
    @RequestMapping(value = "/baseinfo")
    public void infoHe(HttpServletResponse response) {
        File file = new File("/Users/wangtao/Downloads/IMG_3958.PNG");
        FileInputStream fis;
        fis = new FileInputStream(file);
        long size = file.length();
        byte[] temp = new byte[(int) size];
        fis.read(temp, 0, (int) size);
        fis.close();
        byte[] data = temp;
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
        out.close();
    }

}
