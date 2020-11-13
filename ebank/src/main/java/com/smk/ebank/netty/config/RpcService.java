package com.smk.ebank.netty.config;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Description: 标识哪些服务是提供rpc服务的
 * @author:  liuhui
 * @version: 1.0
 * @since:   JDK 1.8
 * @Date:    2020/11/11 0:06
 * Copyright (c) , bwton.com All Rights Reserved.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

}
