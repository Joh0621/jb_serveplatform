package com.bonc.jibei;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 * @Author: dupengling
 * @DateTime: 2022/4/18 22:40
 * @Description: TODO
 */
@SpringBootApplication
@MapperScan("com.bonc.jibei.mapper")
public class RunApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(RunApplication.class, args);
    }
}
