package com.smk.common.netty.message;

import com.smk.common.netty.constant.MsgType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 13:45
 * Copyright (c) 2020
 */
@Getter
@Setter
public class BaseMsgPacket {
    /**
     * 魔数
     */
    private int magicNumber;

    /**
     * 版本号
     */
    private int version;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 消息类型
     */
    private MsgType msgType;

    /**
     * 附件 - K-V形式
     */
    private Map<String, String> attachments = new HashMap<>();

    /**
     * 添加附件
     */
    public void addAttachment(String key, String value) {
        attachments.put(key, value);
    }
}
