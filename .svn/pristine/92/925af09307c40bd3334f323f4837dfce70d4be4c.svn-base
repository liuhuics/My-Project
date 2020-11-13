package com.smk.common.annotation;

import java.lang.annotation.*;

/**
 * @Description: 自定义日志注解，可以拦截有注解的方法，日志中追加注解的value。
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/19 15:58
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target({ ElementType.METHOD })
public @interface BizLog {

    /**
     * 日志描述
     *
     * @return
     */
    String value();

}
