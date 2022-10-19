package com.bonc.jibei.config;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Configuration
public class HiveConfig {
    public List<Map<String, Object>> query(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:hive2://24.43.105.36:10000/src", "hadoop", "hadoop");
        Statement statement = connection.createStatement();
        System.out.println(sql);
        ResultSet rs = statement.executeQuery(sql);
        HashMap<String, Object> hs = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        List<Map<String, Object>> maps = new ArrayList<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                HashMap<String, Object> rowData = new HashMap<>();
                for (int i=1;i<=columnCount;i++){
                    if ("x1".equals(metaData.getColumnName(i))){
                        rowData.put(metaData.getColumnName(i), rs.getString(i).substring(0,rs.getString(i).length()-2));
                    }else {
                        rowData.put(metaData.getColumnName(i),rs.getString(i));
                    }
                }
                String value = rs.getString("y1");
                String x1 = rs.getString("x1");
                hs.put("x1", x1);
                hs.put("y1", value);
                maps.add(rowData);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(maps);
        statement.close();
        connection.close();
        return maps;
    }
}
