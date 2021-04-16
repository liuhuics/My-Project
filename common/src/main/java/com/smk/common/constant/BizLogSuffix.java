package com.smk.common.constant;

/**
 * @Description: 业务日志的后缀
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/20 11:34
 * Copyright (c) , .
 */
public enum BizLogSuffix {

    START("开始"), END("结束"), ERROR("出错");

    private String description;

    BizLogSuffix(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
