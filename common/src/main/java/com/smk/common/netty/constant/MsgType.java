package com.smk.common.netty.constant;

import lombok.Getter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 13:46
 * Copyright (c) 2020
 */
public enum MsgType {
    /**
     * 请求
     */
    REQUEST((byte) 1),

    /**
     * 响应
     */
    RESPONSE((byte) 2),

    /**
     * PING
     */
    PING((byte) 3),

    /**
     * PONG
     */
    PONG((byte) 4),

    /**
     * NULL
     */
    NULL((byte) 5);



    MsgType(Byte type) {
        this.type = type;
    }

    @Getter
    private final Byte type;

    public static MsgType fromValue(byte value) {
        for (MsgType type : MsgType.values()) {
            if (type.getType() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("value = %s", value));
    }
}
