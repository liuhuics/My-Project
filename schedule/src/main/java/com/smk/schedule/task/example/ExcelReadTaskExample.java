package com.smk.schedule.task.example;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/28 10:39
 * Copyright (c) 2019
 */
@Component
@Slf4j
public class ExcelReadTaskExample implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("excel解析任务:");
        log.info("文件名称：" + shardingContext.getJobParameter());
    }
}
