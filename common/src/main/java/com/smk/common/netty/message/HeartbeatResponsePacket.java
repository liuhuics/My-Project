package com.smk.common.netty.message;


import com.smk.common.netty.constant.MsgType;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 13:51
 * Copyright (c) 2020
 */
public class HeartbeatResponsePacket extends  BaseMsgPacket{


    public HeartbeatResponsePacket(RequestMsgPacket requestMsgPacket) {
        this.setMagicNumber(requestMsgPacket.getMagicNumber());
        this.setVersion(requestMsgPacket.getVersion());
        this.setSerialNumber(requestMsgPacket.getSerialNumber());
        this.setAttachments(requestMsgPacket.getAttachments());
        this.setMsgType(MsgType.PONG);
    }
}
