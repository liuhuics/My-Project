package com.smk.common.netty.message;


import com.smk.common.netty.constant.MsgType;
import com.smk.common.netty.constant.NettyConstant;
import com.smk.common.util.SerialNumberUtils;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 13:51
 * Copyright (c) 2020
 */
public class HeartbeatRequestPacket extends  BaseMsgPacket{


    public HeartbeatRequestPacket() {
        this.setMagicNumber(NettyConstant.MAGIC_NUMBER);
        this.setVersion(NettyConstant.VERSION);
        this.setSerialNumber(SerialNumberUtils.getSeriaNumber());
        this.setMsgType(MsgType.PING);
    }
}
