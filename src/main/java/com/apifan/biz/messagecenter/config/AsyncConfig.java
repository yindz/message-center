package com.apifan.biz.messagecenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步配置
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 最大线程数
     */
    @Value("${msg.maxPoolSize}")
    private Integer msgMaxPoolSize;

    @Value("${sms.maxPoolSize}")
    private Integer smsMaxPoolSize;

    /**
     * 配置用于消息推送的线程池
     * @return
     */
    @Bean
    public AsyncTaskExecutor msgPushThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //如果没有配置，则默认最多50个线程
        int poolSize = 50;
        if(msgMaxPoolSize != null && msgMaxPoolSize > 0){
            poolSize = msgMaxPoolSize;
        }
        executor.setCorePoolSize(poolSize / 2);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(poolSize / 2);
        executor.setKeepAliveSeconds(10);
        
        //当pool已经达到maxSize的时候，抛出RejectedExecutionException异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    /**
     * 配置用于短信发送的线程池
     * @return
     */
    @Bean
    public AsyncTaskExecutor smsSendThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //如果没有配置，则默认最多50个线程
        int poolSize = 50;
        if(smsMaxPoolSize != null && smsMaxPoolSize > 0){
            poolSize = smsMaxPoolSize;
        }
        executor.setCorePoolSize(poolSize / 2);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(poolSize / 2);
        executor.setKeepAliveSeconds(10);

        //当pool已经达到maxSize的时候，抛出RejectedExecutionException异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
