package com.smk.ebank.netty.server.longConnection;

import com.smk.ebank.netty.server.RequestMsgPacketDecoder;
import com.smk.ebank.netty.server.ResponseMsgPacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Description: Netty过滤设置
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/1 16:39
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel ch) {

        ch.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new RequestMsgPacketDecoder())
                .addLast(new ResponseMsgPacketEncoder())
                .addLast(new NettyServerHandler());


    }
}
