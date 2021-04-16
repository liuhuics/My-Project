package com.smk.ebank.service.xy.impl;

import com.smk.api.xy.XyExampleService;
import com.smk.common.util.JsonUtil;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.netty.XyResult;
import com.smk.ebank.netty.config.RpcService;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/31 10:08
 * Copyright (c) 2020
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
    public XyResult sayHelloWithReturnObject(String name) {
        XyResult xyResult = new XyResult();
        xyResult.setObj("object hello " + name);
        return xyResult;
    }

    @Override
    public XyResult sayHelloWithObject(BaseResult baseResult) {
        XyResult xyResult = new XyResult();
        xyResult.setObj("object hello " + JsonUtil.object2Json(baseResult));
        return xyResult;
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
