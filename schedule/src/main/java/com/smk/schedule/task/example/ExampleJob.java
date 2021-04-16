package com.smk.schedule.task.example;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.smk.schedule.annotation.ElasticSimpleJob;
import org.springframework.stereotype.Component;
/**
 * @Description: 测试任务
 * @author:  liuhui
 * @version: 1.0
 * @since:   JDK 1.8
 * @Date:    2019/10/15 16:46
 * Copyright (c) , .
 */
/*@ElasticSimpleJob(cron = "0 * * * * ?")
@Component*/
public class ExampleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext context) {
        System.out.println("come in");
    }
}
