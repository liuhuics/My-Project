package com.smk.business.netty.client.longConnection;

import com.alibaba.fastjson.JSON;
import com.smk.common.netty.message.HeartbeatRequestPacket;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.message.ResponseMsgPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @Description: netty客户端处理类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/27 10:41
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ResponseMsgPacket> implements Callable<ResponseMsgPacket> {

    /*RPC响应对象*/
    private ResponseMsgPacket response;
    /*RPC请求对象*/
    private RequestMsgPacket requestMsgPacket;

    private volatile Channel channel;

    private final Object obj = new Object();


    private ServerAdress serverAdress;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
//        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    public void setRequestMsgPacket(RequestMsgPacket requestMsgPacket) {
        this.requestMsgPacket = requestMsgPacket;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMsgPacket responseMsgPacket) throws Exception {
        log.info("接收到来自服务端的响应消息,消息内容:{}", JSON.toJSONString(responseMsgPacket));
        this.response = responseMsgPacket;
        synchronized (obj) {
            //收到响应，唤醒线程
            obj.notifyAll();
        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 不管是读事件空闲还是写事件空闲都向服务器发送心跳包
            sendHeartbeatPacket(ctx);
        }
    }


    public ResponseMsgPacket send(RequestMsgPacket request) throws Exception {
        log.info("发送请求报文:{}", JSON.toJSONString(request));

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建并初始化Netty客户端bootstrap对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer())
                    .option(ChannelOption.SO_KEEPALIVE, true);

            //连接RPC服务器
            ChannelFuture future = bootstrap.connect("", 9092).sync();
            //写入RPC请求数据
            future.channel().writeAndFlush(request).sync();

            synchronized (obj) {
                //未收到响应，使线程继续等待
                obj.wait();
            }

            if (null != response) {
                //关闭RPC请求连接
                future.channel().closeFuture().sync();//获取Channel的CloseFuture,并阻塞当前线程直到它完成
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }


    /**
     * 发送心跳包
     *
     * @param ctx
     */
    private void sendHeartbeatPacket(ChannelHandlerContext ctx) {
        HeartbeatRequestPacket heartbeatRequestPacket = new HeartbeatRequestPacket();
        ctx.writeAndFlush(heartbeatRequestPacket);
    }

    @Override
    public ResponseMsgPacket call() throws Exception {
        return send(requestMsgPacket);
    }

    public void setServerAdress(ServerAdress serverAdress) {
        this.serverAdress = serverAdress;
    }
}
