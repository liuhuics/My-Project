package com.smk.schedule.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: Elastic-job 简单作业用注解，省去配置job
 * @author:  liuhui
 * @version: 1.0
 * @since:   JDK 1.8
 * @Date:    2019/10/15 16:50
 * Copyright (c) , .
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSimpleJob {

	@AliasFor("cron")
	String value() default "";

	@AliasFor("value")
	String cron() default "";

	int shardingTotalCount() default 1;

	String shardingItemParameters() default "";

	String dataSource() default "";

	boolean disabled() default false;
}
