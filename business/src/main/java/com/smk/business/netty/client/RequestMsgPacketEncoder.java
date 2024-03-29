package com.smk.business.netty.client;

import com.smk.common.netty.constant.NettyConstant;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.util.FastJsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Map;

/**
 * @Description: 请求体编码
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 14:04
 * Copyright (c) 2020
 */
public class RequestMsgPacketEncoder extends MessageToByteEncoder<RequestMsgPacket> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RequestMsgPacket packet, ByteBuf out) throws Exception {
        // 魔数
        out.writeInt(packet.getMagicNumber());
        // 版本
        out.writeInt(packet.getVersion());
        // 流水号
        out.writeInt(packet.getSerialNumber().length());
        out.writeCharSequence(packet.getSerialNumber(), NettyConstant.PROTOCOL_ENCODING);
        // 消息类型
        out.writeByte(packet.getMsgType().getType());
        // 附件size
        Map<String, String> attachments = packet.getAttachments();
        out.writeInt(attachments.size());
        // 附件内容
        attachments.forEach((k, v) -> {
            out.writeInt(k.length());
            out.writeCharSequence(k, NettyConstant.PROTOCOL_ENCODING);
            out.writeInt(v.length());
            out.writeCharSequence(v, NettyConstant.PROTOCOL_ENCODING);
        });
        // 接口全类名
        out.writeInt(packet.getInterfaceName().length());
        out.writeCharSequence(packet.getInterfaceName(), NettyConstant.PROTOCOL_ENCODING);
        // 方法名
        out.writeInt(packet.getMethodName().length());
        out.writeCharSequence(packet.getMethodName(), NettyConstant.PROTOCOL_ENCODING);

        if (null != packet.getMethodArgumentClasses()) {
            int len = packet.getMethodArgumentClasses().length;
            // 方法参数签名数组长度
            out.writeInt(len);
            for (int i = 0; i < len; i++) {
                byte[] bytes = FastJsonSerializer.INSTANCE.encode(packet.getMethodArgumentClasses()[i]);
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            }
        } else {
            out.writeInt(0);
        }

        // 方法参数(Object[]类型) - 非必须
        if (null != packet.getMethodArguments()) {
            int len = packet.getMethodArguments().length;
            // 方法参数数组长度
            out.writeInt(len);
            for (int i = 0; i < len; i++) {
                byte[] bytes = FastJsonSerializer.INSTANCE.encode(packet.getMethodArguments()[i]);
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            }
        } else {
            out.writeInt(0);
        }

        Class returnClass = packet.getMethodReturnType();
        if (null != returnClass) {
            byte[] bytes = FastJsonSerializer.INSTANCE.encode(returnClass);
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        } else {
            out.writeInt(0);
        }
    }
}
