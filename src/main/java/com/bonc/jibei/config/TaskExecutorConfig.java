package com.bonc.jibei.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: dupengling
 * @DateTime: 2022/5/4 23:36
 * @Description: 任务配置
 */
@Configurable
@EnableAsync
//所有定时任务都放在一个线程池中，定时任务启动时使用不同的线程
public class TaskExecutorConfig {
    private static final int corePoolSize=10;//默认线程数
    private static final int maxPoolSize=100;//最大线程数
    private static final int keepAliveTime=10;//允许线程空闲时间(单位默认为秒)，10秒后就把线程关闭
    private static final int queueCapacity=200;// 缓冲队列数
    private static final String threadNamePrefix="report_";//线程池名前缀
    @Bean("threadPoolTaskExecutor")//bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor getTaskthread(){
        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        //线程池拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        executor.initialize();
        return executor;
    }
}
