package com.smk.business.controller;

import com.smk.api.xy.XyExampleService;
import com.smk.business.netty.client.shortConnection.NettyProxy4Short;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/31 12:10
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class NettyApiController {

    @Autowired
    private NettyProxy4Short nettyProxy4Short;

    @GetMapping("/test")
    public BaseResult test(String param) {
        XyExampleService xyExampleService = nettyProxy4Short.create(XyExampleService.class);
        if (param.equals("object")) {
            BaseResult baseResult = BaseResultBuilder.buildSuccessResult();
            return xyExampleService.sayHelloWithObject(baseResult);
        } else if (param.equals("void")) {
            xyExampleService.sayHello(param);
            return null;
        } else if (param.equals("return")) {
            return xyExampleService.sayHelloWithReturnObject(param);
        } else if (param.equals("exception")) {
            xyExampleService.sayHelloWithException(param);

        } else if (param.equals("string")) {
            return BaseResultBuilder.buildBadRequestResult(xyExampleService.sayHello(1));

        } else if (param.equals("int")) {
            return BaseResultBuilder.buildBadRequestResult(xyExampleService.sayHelloInt(1) + "");

        } else if (param.equals("integer")) {
            return BaseResultBuilder.buildBadRequestResult(xyExampleService.sayHelloInteger(1) + "");

        } else if (param.equals("no")) {
            xyExampleService.sayHello();

        }
        return BaseResultBuilder.buildSuccessResult();
    }

}
