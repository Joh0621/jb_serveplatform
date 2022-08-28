package com.bonc.jibei.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bonc.jibei.api.DataSourceEnum;
import com.bonc.jibei.api.ErrorTypeEnum;
import com.bonc.jibei.api.Result;
import com.bonc.jibei.config.HiveConfig;
import com.bonc.jibei.config.influx.InfluxDBClient;
import com.bonc.jibei.entity.DataQualityError;
import com.bonc.jibei.entity.Influx;
import com.bonc.jibei.entity.PassRateStatistics;
import com.bonc.jibei.entity.Qualified;
import com.bonc.jibei.mapper.DataQualityErrorMapper;
import com.bonc.jibei.service.DataQualityErrorService;
import com.bonc.jibei.util.DateUtil;
import com.bonc.jibei.vo.DataQualityErrorVo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description data_quality_error
 * @author wangtao
 * @date 2022-08-08
 */
@Service
public class DataQualityErrorServiceImpl implements DataQualityErrorService {

    @Resource
    private DataQualityErrorMapper dataQualityErrorMapper;
    @Resource
    private InfluxDBClient influxDBClient;
    @Resource
    private HiveConfig hiveConfig;
    @Override
    public Object insert(DataQualityError dataQualityError) {

        // valid
        if (dataQualityError == null) {
            return Result.error(500,"必要参数缺失");
        }

        dataQualityErrorMapper.insert(dataQualityError);
        return Result.ok();
    }


    @Override
    public Object delete(int id) {
        int ret = dataQualityErrorMapper.delete(id);
        return ret>0?Result.ok():Result.error(500,"操作失败");
    }


    @Override
    public Object update(DataQualityError dataQualityError) {
        int ret = dataQualityErrorMapper.update(dataQualityError);
        return ret>0?Result.ok():Result.error(500,"操作失败");
    }


    @Override
    public DataQualityError load(int id) {
        return dataQualityErrorMapper.load(id);
    }

    @Override
    public PassRateStatistics passRateStatistics(String startTime, String endTime, String type,String stationId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<PassRateStatistics> passRateStatisticsList = dataQualityErrorMapper.passRateStatistics(startTime, endTime, type,stationId);
        PassRateStatistics PassRateStatistics = new PassRateStatistics();
        Integer total = 0;
        Integer QualifiedNum = 0;
        for (PassRateStatistics ps:passRateStatisticsList){
          //传入参数为全部时
            if (type==null||"".equals(type)||type.equals(0)){
                total+=ps.getTotalNum();
                QualifiedNum+=ps.getQualifiedNum();
                PassRateStatistics.setTotalNum(total);
                PassRateStatistics.setQualifiedNum(QualifiedNum);
                //总合格率
                PassRateStatistics.setQualifiedRate( Double.valueOf( NumberUtil.round((Double.valueOf(QualifiedNum)/total*100), 2).toString()));
               if (ps.getTypeId().equals(1)){
                   PassRateStatistics.setWindQualifiedRate(ps.getQualifiedRate());
               }else if (ps.getTypeId().equals(2)){
                   PassRateStatistics.setPVQualifiedRate(ps.getQualifiedRate());
               }
            }else {
                BeanUtil.copyProperties(ps,PassRateStatistics);
                if (ps.getTypeId().equals(1)){
                    PassRateStatistics.setWindQualifiedRate(ps.getQualifiedRate());
                }else if (ps.getTypeId().equals(2)){
                    PassRateStatistics.setPVQualifiedRate(ps.getQualifiedRate());
                }

            }
        }
        return PassRateStatistics;
    }

    @Override
    public List<Map<String, Object>> selPassRateTrend(String startTime, String endTime, String type, String stationId,String dataFlag) {
        List<Qualified> qualifieds = dataQualityErrorMapper.SelPassRateTrend(startTime, endTime, type,stationId,dataFlag);
        List<Map<String,Object>> windList = new ArrayList<>();
        for (Qualified qualified:qualifieds){
            Map<String, Object> map = new HashMap<>();
            map.put("hlpt",qualified.getHlpt() );
            map.put("djxt", qualified.getDjxt() );
            map.put("czsssj", qualified.getCzsssj() );
            map.put("glycsj", qualified.getGlycsj() );
            map.put("passRate",qualified.getPassRate());
            if (dataFlag==null||"".equals(dataFlag)||"1".equals(dataFlag)){
                map.put("date", qualified.getDateTime());
            }else {
                map.put("stationName", qualified.getStationId());
            }
            windList.add(map);
        }
        return windList;
    }

    @Override
    public List<DataQualityError> selErrorRecord(IPage<?> page,String dataSource, String errorType, String stationId, String DeviceId) {
        List<DataQualityError> dataQualityErrors = dataQualityErrorMapper.selErrorRecord(page,dataSource, errorType, stationId, DeviceId);
        for (DataQualityError err:dataQualityErrors){

            if (err.getErrorType()!=null&&err.getErrorType()!="") {
                switch (err.getErrorType()) {
                    case "1":
                        err.setErrorType(ErrorTypeEnum.MISS.getMessage());
                        break;
                    case "2":
                        err.setErrorType(ErrorTypeEnum.DEAD.getMessage());
                        break;
                    case "3":
                        err.setErrorType(ErrorTypeEnum.ERROR.getMessage());
                        break;
                }
            }

            if (err.getDataSource()!=null&&err.getDataSource()!="") {
                switch (err.getDataSource()) {
                    case "1":
                        err.setDataSource(DataSourceEnum.HLPT.getMessage());
                        break;
                    case "2":
                        err.setDataSource(DataSourceEnum.DDXT.getMessage());
                        break;
                    case "3":
                        err.setDataSource(DataSourceEnum.CZSSSJ.getMessage());
                        break;
                    case "4":
                        err.setDataSource(DataSourceEnum.GLYCSJ.getMessage());
                        break;
                }
            }
        }
        return dataQualityErrors;
    }




    public Map<String, Object> selErrorDistributed(String startTime, String endTime, String type, String stationId) {
        List<DataQualityErrorVo> qualifieds = dataQualityErrorMapper.selErrorDistributedForData(startTime, endTime, type,stationId);
        List<DataQualityErrorVo> qualifiedsError = dataQualityErrorMapper.selErrorDistributedForError(startTime, endTime, type,stationId);
        List<String> hlptList = new ArrayList<>();
        List<String>djxtList = new ArrayList<>();
        List<String> czsssjList = new ArrayList<>();
        List<String> glycsjList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        List<String> missList = new ArrayList<>();
        List<String> deadList = new ArrayList<>();
        List<String> xList1 = new ArrayList<>();
        List<String> xList2 = new ArrayList<>();
        Map resultMap = new HashMap();
        List<Map<String,Object>> result = new ArrayList<>();
        for (DataQualityErrorVo qualified:qualifieds){
            if (qualified.getDataSource()!=null&&qualified.getDataSource()!="") {
                switch (qualified.getDataSource()) {
                    case "1":
                        qualified.setDataSource(DataSourceEnum.HLPT.getMessage());
                        break;
                    case "2":
                        qualified.setDataSource(DataSourceEnum.DDXT.getMessage());
                        break;
                    case "3":
                        qualified.setDataSource(DataSourceEnum.CZSSSJ.getMessage());
                        break;
                    case "4":
                        qualified.setDataSource(DataSourceEnum.GLYCSJ.getMessage());
                        break;
                }
            }
            xList1.add(qualified.getDataSource());
            errorList.add(qualified.getErrorData());
            missList.add(qualified.getMissData());
            deadList.add(qualified.getDeadData());
        }
        resultMap.put("errorList",errorList);
        resultMap.put("missList",missList);
        resultMap.put("deadList",deadList);
        resultMap.put("xList1",xList1);

        for (DataQualityErrorVo qualified:qualifiedsError){
            hlptList.add(qualified.getHlpt());
            djxtList.add(qualified.getDjxt());
            czsssjList.add(qualified.getCzsssj());
            glycsjList.add(qualified.getGlycsj());
            if (qualified.getErrorType()!=null&&qualified.getErrorType()!="") {
                switch (qualified.getErrorType()) {
                    case "1":
                        qualified.setErrorType(ErrorTypeEnum.MISS.getMessage());
                        break;
                    case "2":
                        qualified.setErrorType(ErrorTypeEnum.DEAD.getMessage());
                        break;
                    case "3":
                        qualified.setErrorType(ErrorTypeEnum.ERROR.getMessage());
                        break;
                }
            }
            xList2.add(qualified.getErrorType());
        }
        resultMap.put("hlptList",hlptList);
        resultMap.put("djxtList",djxtList);
        resultMap.put("czsssjList",czsssjList);
        resultMap.put("glycsjList",glycsjList);
        resultMap.put("xList2",xList2);
        return resultMap;
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> errorDataStatistics(String startTime, String endTime, String stationId, String dataSource, String errorType,  String code) {
//        dataQualityErrorMapper.errorDataStatistics(startTime, endTime, stationId, dataSource,errorType,code);
//        List<Influx> influxList = influxDBClient.query("SELECT *  FROM iot_tmp   limit 10", Influx.class);
        //假如数据源是场站实时数据，就去influxdb查询
        String type="";
        List<Map<String, Object>> list = new ArrayList<>();
        if ("3".equals(dataSource)) {
            String value = "SELECT * FROM \"iot_ncepri\" WHERE time > now() - 5m limit 100 tz('Asia/Shanghai') ";
            List<Influx> influxList = influxDBClient.query(value, Influx.class);
            if(null!=influxList&&influxList.size()>0){
                for(Influx influxResult:influxList){
                    Map<String, Object> map = new HashMap<>();
                    String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(influxResult.getDatetime());
                    Double value1 = influxResult.getValue();
                    map.put("date", date);
                    map.put("value", value1);
                    list.add(map);
                }
            }
        }else if ("1".equals(dataSource)){
            String table="";
            String cxjg="";
            String cxtj="";
            if(null!=errorType&&"".equals(errorType)){
                if ("1".equals(dataSource)){
                    //海量平台
                    table="src_hlpt_calcpoint_d";
                }else if ("2".equals(dataSource)){
                    //单机-光伏
                    if ("2".equals(type)){
                        table="src_danji_sun_format_d";
                        cxtj="station='' and gfid='1#INY'";
                    }else {
                        //单机-风电
                        table="src_danji_wind_format_d";
                        cxtj="station='' and fjid='49#FJ'";
                    }
                }else if ("4".equals(dataSource)){
                    //功率预测-光伏
                    cxtj="tag=''";
                    if ("2".equals(type)){
                        table="src_glyc_sungrnx_d";
                    }else {
                        //功率预测-风电
                        table="src_glyc_windgnrx_d";
                    }

                }
            }
//         String sql="select datatime as time,spd as value from src.src_danji_wind_format_d a where a.fjid='49#FJ' ";
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            Connection connection = DriverManager.getConnection("jdbc:hive2://24.43.105.36:10000/src", "hadoop", "hadoop");
            Statement statement = connection.createStatement();
            System.out.println("++++++++++++++");
            ResultSet rs = statement.executeQuery("SELECT  from_unixtime(unix_timestamp(a.ddate),'yyyy-MM-dd HH:mm:ss') as x1,      a.value as y1 ,     round(cast(b.value as double ),2) as  y2         FROM src.src_glyc_windgnrx_d a     left join src.src_hlpt_calcpoint_d b      on REGEXP_REPLACE( REGEXP_REPLACE(REGEXP_REPLACE(a.ddate,'-','') ,':',''),' ','')=CONCAT (b.ddate,b.ttime,'00')      where  a.tag ='dawindfore70' and a.day=REGEXP_REPLACE(SUBSTR('2022-03-13 00:00:00',0,10) ,'-','')     and  b.id ='121878665217709300' and b.day=REGEXP_REPLACE(SUBSTR('2022-03-14 00:00:00',0,10) ,'-','')     ORDER  by  a.ddate \n");
            while (rs.next()){
                String value = rs.getString("y1");
                System.out.println("aaaaa"+value);
            }
            statement.close();
            connection.close();
            list = hiveConfig.query("SELECT  from_unixtime(unix_timestamp(a.ddate),'yyyy-MM-dd HH:mm:ss') as x1,      a.value as y1 ,     round(cast(b.value as double ),2) as  y2         FROM src.src_glyc_windgnrx_d a     left join src.src_hlpt_calcpoint_d b      on REGEXP_REPLACE( REGEXP_REPLACE(REGEXP_REPLACE(a.ddate,'-','') ,':',''),' ','')=CONCAT (b.ddate,b.ttime,'00')      where  a.tag ='dawindfore70' and a.day=REGEXP_REPLACE(SUBSTR('2022-03-13 00:00:00',0,10) ,'-','')     and  b.id ='121878665217709300' and b.day=REGEXP_REPLACE(SUBSTR('2022-03-14 00:00:00',0,10) ,'-','')     ORDER  by  a.ddate \n");

        }
        //否则就查询Hive
//        System.out.println(influxList);
        return list;
    }

//    @Override
//    public Map<String,Object> pageList(int offset, int pagesize) {
//
//        List<DataQualityError> pageList = (List<DataQualityError>) dataQualityErrorMapper.pageList(offset, pagesize);
//        int totalCount = dataQualityErrorMapper.pageListCount(offset, pagesize);
//
//        // result
//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("pageList", pageList);
//        result.put("totalCount", totalCount);
//
//        return result;
//    }

}
