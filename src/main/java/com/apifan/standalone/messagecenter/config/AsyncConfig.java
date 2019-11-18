package com.apifan.standalone.messagecenter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    //默认线程池大小
    private static final int DEFAULT_POOL_SIZE = 4;

    @Autowired
    private MessageProperties messageProperties;

    /**
     * 配置用于app消息推送的线程池
     *
     * @return
     */
    @Bean
    public AsyncTaskExecutor appMsgThreadPool() {
        return createThreadPoolTaskExecutor(messageProperties.getAppMsgPoolSize(), "appmsg-thread-");
    }

    /**
     * 配置用于短信发送的线程池
     *
     * @return
     */
    @Bean
    public AsyncTaskExecutor smsThreadPool() {
        return createThreadPoolTaskExecutor(messageProperties.getSmsPoolSize(), "sms-thread-");
    }

    /**
     * 配置用于邮件发送的线程池
     *
     * @return
     */
    @Bean
    public AsyncTaskExecutor emailThreadPool() {
        return createThreadPoolTaskExecutor(messageProperties.getEmailPoolSize(), "email-thread-");
    }

    /**
     * 创建线程池
     *
     * @param configuredPoolSize 配置的线程池大小
     * @param prefix             线程名前缀
     * @return 线程池实例
     */
    private ThreadPoolTaskExecutor createThreadPoolTaskExecutor(int configuredPoolSize, String prefix) {
        int poolSize = Math.max(configuredPoolSize, DEFAULT_POOL_SIZE);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(poolSize * 2048);
        executor.setKeepAliveSeconds(0);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(prefix);
        logger.info("线程池大小为: {}, 前缀: {}", poolSize, prefix);
        return executor;
    }
}
