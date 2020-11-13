package com.smk.business.netty.client.shortConnection;

import com.alibaba.fastjson.JSON;
import com.smk.business.netty.client.RequestMsgPacketEncoder;
import com.smk.business.netty.client.ResponseMsgPacketDecoder;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.message.ResponseMsgPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
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
    /*主机名*/
    private String host;
    /*端口*/
    private int port;
    /*RPC响应对象*/
    private ResponseMsgPacket response;
    /*RPC请求对象*/
    private RequestMsgPacket requestMsgPacket;

    private final Object obj = new Object();

    public NettyClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
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


    public ResponseMsgPacket send(RequestMsgPacket request) throws Exception {
        log.info("发送请求报文:{}", JSON.toJSONString(request));

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建并初始化Netty客户端bootstrap对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()

                                    .addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
                                    .addLast(new LengthFieldPrepender(4))
                                    /*将RPC请求进行编码（发送请求）*/
                                    .addLast(new RequestMsgPacketEncoder())
                                    /*将RPC响应进行解码（返回响应）*/
                                    .addLast(new ResponseMsgPacketDecoder())
                                    .addLast(NettyClientHandler.this);
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            //连接RPC服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
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
            group.shutdownGracefully().sync();
        }
    }


    @Override
    public ResponseMsgPacket call() throws Exception {
        return send(requestMsgPacket);
    }
}
