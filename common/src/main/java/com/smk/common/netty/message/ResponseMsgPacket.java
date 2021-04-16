package com.smk.common.netty.message;

import com.smk.common.netty.constant.MsgType;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 13:39
 * Copyright (c) 2020
 */
@Setter
@Getter
public class ResponseMsgPacket extends BaseMsgPacket {
    /**
     * error code
     */
    private Long errorCode;

    /**
     * 消息描述
     */
    private String message;

    /**
     * 消息载荷,传输时，会将其转为 ByteBuf
     */
    private Object payload;

    private Class<?> methodReturnType;

    public ResponseMsgPacket(RequestMsgPacket requestMsgPacket) {
        this.setMagicNumber(requestMsgPacket.getMagicNumber());
        this.setVersion(requestMsgPacket.getVersion());
        this.setSerialNumber(requestMsgPacket.getSerialNumber());
        this.setAttachments(requestMsgPacket.getAttachments());
        this.setMsgType(MsgType.RESPONSE);
        this.setAttachments(requestMsgPacket.getAttachments());
        this.setMethodReturnType(requestMsgPacket.getMethodReturnType());

    }

    public ResponseMsgPacket() {
    }
}
