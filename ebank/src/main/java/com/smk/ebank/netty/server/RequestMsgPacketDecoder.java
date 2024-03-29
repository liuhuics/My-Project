package com.smk.ebank.netty.server;

import com.google.common.collect.Maps;
import com.smk.common.netty.constant.MsgType;
import com.smk.common.netty.constant.NettyConstant;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.util.FastJsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 请求体解码
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 14:04
 * Copyright (c) 2020
 */
@Slf4j
public class RequestMsgPacketDecoder extends ByteToMessageDecoder {
    //ByteBuf不需要调用flip()来切换读写
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        int length = in.readableBytes();
        if (length < 13) {
            log.info("报文长度不够");
            return;
        }
        int magicNum = in.readInt();
        if (magicNum != NettyConstant.MAGIC_NUMBER) { // 魔数校验不通过，则关闭连接
            log.error("魔数校验失败");
            in.clear();
            channelHandlerContext.channel().close();
            return;
        }
        RequestMsgPacket packet = new RequestMsgPacket();
        // 魔数
        packet.setMagicNumber(magicNum);
        // 版本
        packet.setVersion(in.readInt());
        // 流水号
        int serialNumberLength = in.readInt();
        packet.setSerialNumber(in.readCharSequence(serialNumberLength, NettyConstant.PROTOCOL_ENCODING).toString());
        // 消息类型
        byte MsgTypeByte = in.readByte();
        packet.setMsgType(MsgType.fromValue(MsgTypeByte));
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
        // 接口全类名
        int interfaceNameLength = in.readInt();
        packet.setInterfaceName(in.readCharSequence(interfaceNameLength, NettyConstant.PROTOCOL_ENCODING).toString());
        // 方法名
        int methodNameLength = in.readInt();
        packet.setMethodName(in.readCharSequence(methodNameLength, NettyConstant.PROTOCOL_ENCODING).toString());

        // 方法参数签名
        int methodArgumentClzArrayLength = in.readInt();
        if (methodArgumentClzArrayLength > 0) {
            Class[] argClass = new Class[methodArgumentClzArrayLength];
            for (int i = 0; i < methodArgumentClzArrayLength; i++) {
                byte[] bytes = new byte[in.readInt()];
                in.readBytes(bytes);

                Class arg = (Class) FastJsonSerializer.INSTANCE.decode(bytes, Class.class);
                argClass[i] = arg;
            }

            packet.setMethodArgumentClasses(argClass);

        }

        // 方法参数
        parseArgs(packet, in);

        byte[] returnTypeBytes = new byte[in.readInt()];
        in.readBytes(returnTypeBytes);
        Class returnType = (Class) FastJsonSerializer.INSTANCE.decode(returnTypeBytes, Class.class);
        packet.setMethodReturnType(returnType);
        list.add(packet);
    }


    /**
     * 解析方法参数
     *
     * @param packet
     * @param in
     */
    private void parseArgs(RequestMsgPacket packet, ByteBuf in) {
        int methodArgumentArrayLength = in.readInt();
        if (methodArgumentArrayLength == 0) {
            return;
        }

        Class[] signaturesClasses = packet.getMethodArgumentClasses();
        int signaturesLength = signaturesClasses.length;


        int size = signaturesLength >= methodArgumentArrayLength ? methodArgumentArrayLength : signaturesLength;

        Object[] args = new Object[signaturesLength];
        Arrays.fill(args, null);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                byte[] bytes = new byte[in.readInt()];
                in.readBytes(bytes);

                Object arg = FastJsonSerializer.INSTANCE.decode(bytes, signaturesClasses[i]);
                args[i] = arg;
            }
            packet.setMethodArguments(args);
        }


    }
}
