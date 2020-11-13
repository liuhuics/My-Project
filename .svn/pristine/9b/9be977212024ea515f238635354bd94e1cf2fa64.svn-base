package com.smk.common.constant.enums;

/**
 * @Description:错误信息常量类
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/3 16:18
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
public enum ErrorInfoEnum {

    /**
     * 文件名称重复
     */
    FILE_DUPLICATE(900, "文件名称重复"),

    /**
     * 文件正在处理中
     */
    FILE_PROCESSING(901, "文件正在处理中"),

    /**
     * 产品编号不存在
     */
    PRODUCT_ID_NOT_EXISTS(902, "产品编号不存在"),

    /**
     * 活动编号不存在
     */
    ACTIVITY_ID_NOT_EXISTS(903, "活动编号不存在");

    ErrorInfoEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
