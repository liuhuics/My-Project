package com.smk.common.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 自定义日志常量类
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/19 15:58
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
public class CustomLogType {

    public static final Logger BIZ_INFO = getLogger(LogConstant.BIZ_INFO);

    public static final Logger HTTP_INFO = getLogger(LogConstant.HTTP_INFO);

    public static final Logger LONG_TIME = getLogger(LogConstant.LONG_TIME);

    public static Logger getLogger(String logType) {
        return LoggerFactory.getLogger(logType);
    }
}