package com.smk.schedule.task.example;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
/**
 * @Description: 普通定时作业
 * @author:  liuhui
 * @version: 1.0
 * @since:   JDK 1.8
 * @Date:    2019/10/15 16:48
 * Copyright (c) , .
 */
//@ElasticSimpleJob(cron = "0 * * * * ?",shardingTotalCount = 3,shardingItemParameters = "0=Beijing,1=Shanghai,2=Guangzhou")
//@Component
public class MySimpleJob implements SimpleJob {


    @Override
    public void execute(ShardingContext context) {
        switch (context.getShardingItem()) {//如果分片数多于1时才需要这么写
            case 0:
                // do something by sharding item 0
                break;
            case 1:
                // do something by sharding item 1
                break;
            case 2:
                // do something by sharding item 2
                break;
            // case n: ...
        }
    }
}
