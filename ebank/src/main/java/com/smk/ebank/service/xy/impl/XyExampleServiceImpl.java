package com.smk.ebank.service.xy.impl;

import com.smk.api.xy.XyExampleService;
import com.smk.common.util.JsonUtil;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.ebank.netty.config.RpcService;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/31 10:08
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@RpcService
public class XyExampleServiceImpl implements XyExampleService {
    @Override
    public void sayHello(String name) {
        System.out.println("void hello " + name);
    }

    @Override
    public String sayHello(int name) {
        return "abc";
    }

    @Override
    public int sayHelloInt(int name) {
        return 1;
    }


    @Override
    public BaseResult sayHelloWithReturnObject(String name) {
        return BaseResultBuilder.buildBadRequestResult("object hello " + name);
    }

    @Override
    public BaseResult sayHelloWithObject(BaseResult baseResult) {
        return BaseResultBuilder.buildBadRequestResult("object hello " + JsonUtil.object2Json(baseResult));
    }

    @Override
    public void sayHelloWithException(String name) {
        Object o = null;
        System.out.println(o.toString());
    }

    @Override
    public Integer sayHelloInteger(Integer name) {
        return 1;
    }

    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
