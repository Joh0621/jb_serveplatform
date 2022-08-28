package com.bonc.jibei.config.influx;

import org.influxdb.InfluxDB;
import org.influxdb.dto.*;
import org.influxdb.impl.InfluxDBResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * gb_epplatform
 * Created at 2019/09/26 09:42
 *
 * @author renguangli
 * @since JDK1.8
 */
@Component
public class InfluxDBClient {

    private static final Logger logger = LoggerFactory.getLogger(InfluxDBClient.class);

    @Resource
    private InfluxDB influxDB;

    @Value("${spring.influx.database}")
    private String dbName;

    private String retentionPolicy;


    /**
     * ex. select * from user where username = $username and age > $age
     * command must contains space. (username = $username,age > $age)
     *
     * @param command    influx query command
     * @param clazz      class
     * @param parameters parameters
     * @param <T>        t
     * @return list<T>
     */
    public <T> List<T> query(String command, Class<T> clazz, Object... parameters) {
        // 将查询结果转化为对象
        QueryResult queryResult = query(command, parameters);
        InfluxDBResultMapper influxDBResultMapper = new InfluxDBResultMapper();
        return influxDBResultMapper.toPOJO(queryResult, clazz);
    }

    /**
     * ex. select * from user where username = $username and age > $age
     * command must contains space. (username = $username,age > $age)
     *
     * @param command    influx query command
     * @param parameters parameters
     * @return QueryResult
     */
    public QueryResult query(String command, Object... parameters) {
        // 构建 query
        BoundParameterQuery.QueryBuilder queryBuilder = BoundParameterQuery.QueryBuilder
                .newQuery(command)
                .forDatabase(this.dbName);

        // 绑定占位符参数，类似 prepareStatement
        if (parameters.length > 0) {
            String[] strs = command.split("\\s+");
            List<String> fields = new ArrayList<>();
            Arrays.stream(strs).filter(str -> str.startsWith("$"))
                    .forEach(str -> fields.add(str.substring(1)));
            for (int i = 0; i < parameters.length; i++) {
                queryBuilder.bind(fields.get(i), parameters[i]);
            }
        }
        return influxDB.query(queryBuilder.create());
    }

    /**
     * 创建自定义保留策略
     *
     * @param policyName  策略名
     * @param duration    保存时间 h（小时），d（天），w（星期）；
     * @param shardGroup  负责指定时间跨度的数据存储，这个时间跨度就由上文提到的创建RP时指定。如果没有指定，系统将通过RP的数据保留时间来计算。
     *                    h（小时），d（天），w（星期）；
     * @param replication 保存副本数量 副本个数，决定在集群模式下数据副本的个数。InfluxDB在N个数据节点上复制数据，其中N就是replication。一般为1
     * @param isDefault   是否设为默认保留策略
     */
    public QueryResult createRetentionPolicy(String policyName, String dbName, String duration, String shardGroup, int replication, Boolean isDefault) {
        String sql = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s SHARD DURATION %s ", policyName,
                dbName, duration, replication, shardGroup);
        if (isDefault) {
            sql = sql + " DEFAULT";
        }
        return execute(sql);
    }

    public QueryResult createDatabase(String dbName) {
        String command = "create database " + dbName;
        return execute(command);
    }

    /**
     * @param command influx query command
     * @return QueryResult
     */
    public QueryResult execute(String command) {
        return influxDB.query(new Query(command));
    }

    public <T> void write(List<T> points) {
        this.write(points, this.retentionPolicy);
    }

    public <T> void write(List<T> points, String rp) {
        BatchPoints batchPoints = BatchPoints.database(this.dbName)
                .precision(TimeUnit.MILLISECONDS)
                .retentionPolicy(rp).build();
        for (T point : points) {
            long time = System.currentTimeMillis();
            batchPoints.point(Point.measurementByPOJO(point.getClass())
                    .time(time, TimeUnit.MILLISECONDS)
                    .addFieldsFromPOJO(point).build());
        }
        this.influxDB.write(batchPoints);
    }

    public <T> void write(List<T> points, long timestamp) {
        BatchPoints batchPoints = BatchPoints.database(this.dbName)
                .precision(TimeUnit.MILLISECONDS).build();
        for (T point : points) {
            batchPoints.point(Point.measurementByPOJO(point.getClass())
                    .time(timestamp, TimeUnit.MILLISECONDS)
                    .addFieldsFromPOJO(point).build());
        }
        this.influxDB.write(batchPoints);
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }

    public void batchInsert(final String database, final String retentionPolicy, final InfluxDB.ConsistencyLevel consistency,
                            final List<String> records) {
        this.influxDB.write(database, retentionPolicy, consistency, records);
    }

    /**
     * 构建Point
     *
     * @param measurement
     * @param time
     * @param fields
     * @return
     */
    public Point pointBuilder(String measurement, long time, Map<String, String> tags, Map<String, Object> fields) {
        Point point = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS).tag(tags).fields(fields).build();
        return point;
    }

    /*
     * 单条插入：单条tag单条field
     * */
    public void insert3(String tags, Integer fields,String measurement,Long timeStamp) {

        Point point = Point.measurement(measurement)
                .time(timeStamp, TimeUnit.MILLISECONDS)
                .tag("label", tags)
                .addField("value", fields).build();
        influxDB.write("jmeter", "", point);
    }
    public <T> void write(List<T> points, String dbName, String rp, TimeUnit unit) {
        BatchPoints batchPoints = BatchPoints.database(dbName)
                .precision(unit)
                .retentionPolicy(rp).build();
        for (T point : points) {
            long time = System.currentTimeMillis();
            try {
                Field field = point.getClass().getDeclaredField("time");
                field.setAccessible(true);
                time = (long)field.get(point);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            batchPoints.point(Point.measurementByPOJO(point.getClass())
                    .time(time, unit)
                    .addFieldsFromPOJO(point).build());
        }
        this.influxDB.write(batchPoints);
    }

}
