package com.smk.schedule.config.elasticjobconfig;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.smk.schedule.annotation.ElasticDataflowJob;
import com.smk.schedule.annotation.ElasticSimpleJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @Description: Elastic-job启动类，扫描所有有elastic-job相关注解的类，并注册
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/15 16:45
 * Copyright (c) , .
 */
@Configuration
@ConditionalOnExpression("'${elaticjob.zookeeper.server-lists}'.length() > 0")
public class ElasticJobAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ZookeeperRegistryCenter regCenter;

    @PostConstruct
    public void initElasticJob() {
        initElasticSimpleJob();
        //        initElasticDataflowJob();
    }

    public void initElasticSimpleJob() {
        Map<String, SimpleJob> map = applicationContext.getBeansOfType(SimpleJob.class);

        for (Map.Entry<String, SimpleJob> entry : map.entrySet()) {
            SimpleJob simpleJob = entry.getValue();
            ElasticSimpleJob elasticSimpleJob = simpleJob.getClass().getAnnotation(ElasticSimpleJob.class);
            if (elasticSimpleJob == null || elasticSimpleJob.disabled()) {
                continue;
            }
            String cron = StringUtils.defaultIfBlank(elasticSimpleJob.cron(), elasticSimpleJob.value());
            SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(JobCoreConfiguration
                    .newBuilder(simpleJob.getClass().getName(), cron, elasticSimpleJob.shardingTotalCount()).misfire(false)
                    .shardingItemParameters(elasticSimpleJob.shardingItemParameters()).build(),
                    simpleJob.getClass().getCanonicalName());
            LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
                    .overwrite(true).build();

            String dataSourceRef = elasticSimpleJob.dataSource();
            if (StringUtils.isNotBlank(dataSourceRef)) {

                if (!applicationContext.containsBean(dataSourceRef)) {
                    throw new RuntimeException("not exist datasource [" + dataSourceRef + "] !");
                }

                DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceRef);
                JobEventRdbConfiguration jobEventRdbConfiguration = new JobEventRdbConfiguration(dataSource);
                SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, regCenter, liteJobConfiguration,
                        jobEventRdbConfiguration);
                jobScheduler.init();
            } else {
                SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, regCenter, liteJobConfiguration);
                jobScheduler.init();
            }
        }
    }

    public void initElasticDataflowJob() {
        Map<String, DataflowJob> map = applicationContext.getBeansOfType(DataflowJob.class);

        for (Map.Entry<String, DataflowJob> entry : map.entrySet()) {
            DataflowJob dataflowJob = entry.getValue();
            ElasticDataflowJob elasticDataflowJob = dataflowJob.getClass().getAnnotation(ElasticDataflowJob.class);
            if (elasticDataflowJob.disabled()) {
                continue;
            }

            String cron = StringUtils.defaultIfBlank(elasticDataflowJob.cron(), elasticDataflowJob.value());
            DataflowJobConfiguration simpleJobConfiguration = new DataflowJobConfiguration(JobCoreConfiguration
                    .newBuilder(dataflowJob.getClass().getName(), cron, elasticDataflowJob.shardingTotalCount())
                    .shardingItemParameters(elasticDataflowJob.shardingItemParameters()).build(),
                    dataflowJob.getClass().getCanonicalName(), true);
            LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
                    .overwrite(true).build();

            String dataSourceRef = elasticDataflowJob.dataSource();
            if (StringUtils.isNotBlank(dataSourceRef)) {

                if (!applicationContext.containsBean(dataSourceRef)) {
                    throw new RuntimeException("not exist datasource [" + dataSourceRef + "] !");
                }

                DataSource dataSource = (DataSource) applicationContext.getBean(dataSourceRef);
                JobEventRdbConfiguration jobEventRdbConfiguration = new JobEventRdbConfiguration(dataSource);
                SpringJobScheduler jobScheduler = new SpringJobScheduler(dataflowJob, regCenter, liteJobConfiguration,
                        jobEventRdbConfiguration);
                jobScheduler.init();
            } else {
                SpringJobScheduler jobScheduler = new SpringJobScheduler(dataflowJob, regCenter, liteJobConfiguration);
                jobScheduler.init();
            }
        }
    }
}
