package com.smk.common.exception;

/**
 * @Description: 请求参数不合法异常
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/15 10:23
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
public class ParamInvalidException extends RuntimeException {

    public ParamInvalidException(String msg) {
        super(msg);
    }

}
