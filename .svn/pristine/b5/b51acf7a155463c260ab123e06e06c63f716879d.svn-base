package com.smk.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/19 11:22
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 核心线程数
     */
    private static final int corePoolSize = 20;

    /**
     * 最大线程数
     */
    private static final int maxPoolSize = 200;

    /**
     * 队列数
     */
    private static final int queueCapacity = 300;

    private String taskThreadNamePrefix = "taskExecutor-";

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(300);
        executor.setThreadNamePrefix(taskThreadNamePrefix);

        // rejection-policy：当pool已经达到max size的时候，怎样处理新任务
        // CALLER_RUNS：不在新线程中运行任务，而是由调用者所在的线程来运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean("scheduledExecutor")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(20);
    }

}
