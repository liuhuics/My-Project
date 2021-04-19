package com.smk.business.netty.client.longconnection;

import com.smk.business.netty.client.RequestMsgPacketEncoder;
import com.smk.business.netty.client.ResponseMsgPacketDecoder;
import com.smk.common.netty.constant.NettyConstant;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Description: Netty客户端处理器初始化，用于长连接
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/14 15:15
 * Copyright (c) 2020
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new IdleStateHandler(0, 0, NettyConstant.BEAT_INTERVAL))
                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4))
                .addLast(new LengthFieldPrepender(4))
                /*将RPC请求进行编码（发送请求）*/
                .addLast(new RequestMsgPacketEncoder())
                /*将RPC响应进行解码（返回响应）*/
                .addLast(new ResponseMsgPacketDecoder())
                .addLast(new NettyClientHandler());
    }
}
