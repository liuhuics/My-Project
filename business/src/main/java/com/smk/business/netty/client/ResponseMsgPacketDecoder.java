package com.smk.business.netty.client;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.smk.common.exception.BizException;
import com.smk.common.netty.constant.MsgType;
import com.smk.common.netty.constant.NettyConstant;
import com.smk.common.netty.message.ResponseMsgPacket;
import com.smk.common.netty.util.FastJsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Map;

/**
 * @Description:响应体解码
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/26 10:39
 * Copyright (c) 2020
 */
public class ResponseMsgPacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        if (length < 13) {
            throw new BizException("报文长度不够");
        }
        ResponseMsgPacket packet = new ResponseMsgPacket();
        // 魔数
        packet.setMagicNumber(in.readInt());
        // 版本
        packet.setVersion(in.readInt());
        // 流水号
        int serialNumberLength = in.readInt();
        packet.setSerialNumber(in.readCharSequence(serialNumberLength, NettyConstant.PROTOCOL_ENCODING).toString());
        // 消息类型
        byte messageTypeByte = in.readByte();
        packet.setMsgType(MsgType.fromValue(messageTypeByte));
        // 附件
        Map<String, String> attachments = Maps.newHashMap();
        packet.setAttachments(attachments);
        int attachmentSize = in.readInt();
        if (attachmentSize > 0) {
            for (int i = 0; i < attachmentSize; i++) {
                int keyLength = in.readInt();
                String key = in.readCharSequence(keyLength, NettyConstant.PROTOCOL_ENCODING).toString();
                int valueLength = in.readInt();
                String value = in.readCharSequence(valueLength, NettyConstant.PROTOCOL_ENCODING).toString();
                attachments.put(key, value);
            }
        }
        // error code
        packet.setErrorCode(in.readLong());
        // message
        int messageLength = in.readInt();
        packet.setMessage(in.readCharSequence(messageLength, NettyConstant.PROTOCOL_ENCODING).toString());
        // payload - ByteBuf实例
        int payloadLength = in.readInt();

        ByteBuf byteBuf = in.readBytes(payloadLength);
        int readableByteLength = byteBuf.readableBytes();
        byte[] bytes = new byte[readableByteLength];
        byteBuf.readBytes(bytes);

        byte[] returnTypeBytes = new byte[in.readInt()];
        in.readBytes(returnTypeBytes);

        Class returnType = (Class) FastJsonSerializer.INSTANCE.decode(returnTypeBytes, Class.class);
        packet.setMethodReturnType(returnType);

        Object targetPayload = JSON.parseObject(new String(bytes, NettyConstant.PROTOCOL_ENCODING), returnType);
        byteBuf.release();

        packet.setPayload(targetPayload);
        out.add(packet);
    }
}
