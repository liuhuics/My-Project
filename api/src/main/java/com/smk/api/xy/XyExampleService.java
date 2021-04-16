package com.smk.api.xy;

import com.smk.common.vo.BaseResult;
import com.smk.common.vo.netty.XyResult;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/31 10:07
 * Copyright (c) 2020
 */
public interface XyExampleService {

    void sayHello(String name);

    String sayHello(int name);

    int sayHelloInt(int name);

    XyResult sayHelloWithReturnObject(String name);

    XyResult sayHelloWithObject(BaseResult baseResult);

    void sayHelloWithException(String name);

    Integer sayHelloInteger(Integer name);

    void sayHello();
}
