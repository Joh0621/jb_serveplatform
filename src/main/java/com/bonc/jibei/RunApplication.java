package com.bonc.jibei;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @Author: dupengling
 * @DateTime: 2022/4/18 22:40
 * @Description: TODO
 */
@SpringBootApplication
public class RunApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(RunApplication.class, args);
    }
}
