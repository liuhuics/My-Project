package com.smk.common.netty.util;

import com.alibaba.fastjson.JSON;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;

import java.util.Arrays;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 14:14
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
public enum FastJsonSerializer{
    // 单例
    INSTANCE;

    public byte[] encode(Object target) {
        return JSON.toJSONBytes(target);
    }

    public Object decode(byte[] bytes, Class<?> targetClass) {
        return JSON.parseObject(bytes, targetClass);
    }

    public static void main(String[] args) {
        String s = "{\"name\":\"throwable\"}";
        System.out.println(Arrays.toString(FastJsonSerializer.INSTANCE.encode(s)));
        s="hello liuhui";
        BaseResult baseResult= BaseResultBuilder.buildSuccessResult();
        System.out.println(Arrays.toString(FastJsonSerializer.INSTANCE.encode(baseResult)));
    }
}
