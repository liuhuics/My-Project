package com.smk.ebank.netty.server;

import com.alibaba.fastjson.JSON;
import com.smk.common.netty.constant.NettyConstant;
import com.smk.common.netty.message.ResponseMsgPacket;
import com.smk.common.netty.util.ByteBufferUtils;
import com.smk.common.netty.util.FastJsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Description: 响应体编码
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/26 10:38
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
public class ResponseMsgPacketEncoder extends MessageToByteEncoder<ResponseMsgPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMsgPacket packet, ByteBuf out) throws Exception {
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
        // error code
        out.writeLong(packet.getErrorCode());
        // message
        String message = packet.getMessage();
        if (StringUtils.isEmpty(message)) {
            out.writeInt(0);
        } else {
            //如果message为空，调此方法会一直循环
            ByteBufferUtils.INSTANCE.encodeUtf8CharSequence(out, message);
        }
        // payload 转成ByteBuf类型
        Object payLoad = packet.getPayload();
        if (payLoad == null) {
            out.writeInt(0);
        } else {
            byte[] jsonByteByte = JSON.toJSONString(packet.getPayload()).getBytes(NettyConstant.PROTOCOL_ENCODING);
            out.writeInt(jsonByteByte.length);
            out.writeBytes(Unpooled.wrappedBuffer(jsonByteByte));
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
