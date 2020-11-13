package com.smk.schedule.task.example;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: 流式作业
 * 流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去； 非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。
 * <p>
 * 如果采用流式作业处理方式，建议processData处理数据后更新其状态，避免fetchData再次抓取到，从而使得作业永不停止。流式数据处理参照TbSchedule设计，适用于不间歇的数据处理。
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/15 16:47
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
//@ElasticDataflowJob(cron="0 * * * * ?",shardingTotalCount = 3,shardingItemParameters = "0=Beijing,1=Shanghai,2=Guangzhou")
//@Component
public class MyDataflowJob implements DataflowJob {

    @Override
    public List fetchData(ShardingContext shardingContext) {
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s", shardingContext.getShardingItem(),
                new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW FETCH"));
        return null;
    }

    @Override
    public void processData(ShardingContext shardingContext, List data) {
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s", shardingContext.getShardingItem(),
                new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(),
                "DATAFLOW PROCESS"));

    }
}
