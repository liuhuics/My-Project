package com.smk.business.netty.client.longconnection;

import com.alibaba.fastjson.JSON;
import com.smk.common.netty.message.HeartbeatRequestPacket;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.message.ResponseMsgPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: netty客户端处理类，用于长连接
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/27 10:41
 * Copyright (c) 2020
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ResponseMsgPacket> implements Callable<ResponseMsgPacket> {

    /*RPC请求对象*/
    private RequestMsgPacket requestMsgPacket;

    private volatile Channel channel;

    private ConcurrentHashMap<String, RpcFuture> pendingRPC = new ConcurrentHashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
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
        String requestSerialNum = responseMsgPacket.getSerialNumber();
        log.debug("Receive response: " + requestSerialNum);
        RpcFuture rpcFuture = pendingRPC.get(requestSerialNum);
        if (rpcFuture != null) {
            pendingRPC.remove(requestSerialNum);
            rpcFuture.done(responseMsgPacket);
        } else {
            log.warn("Can not get pending response for request id: " + requestSerialNum);
        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 不管是读事件空闲还是写事件空闲都向服务器发送心跳包
            sendHeartbeatPacket(ctx);
        }
    }


    public RpcFuture send(RequestMsgPacket request) throws Exception {
        log.info("发送请求报文:{}", JSON.toJSONString(request));
        RpcFuture rpcFuture = new RpcFuture(request);
        pendingRPC.put(request.getSerialNumber(), rpcFuture);
        try {
            //写入RPC请求数据
            ChannelFuture channelFuture = channel.writeAndFlush(request).sync();

//            return channelFuture.get();
            if (!channelFuture.isSuccess()) {
                log.error("Send request {} error", request.getSerialNumber());
            }
        } catch (InterruptedException e) {
            log.error("Send request exception: " + e.getMessage());
        }
        return rpcFuture;
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
        return (ResponseMsgPacket) send(requestMsgPacket).get();
    }

}
