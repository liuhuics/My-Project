package com.smk.ebank.netty.server.longconnection;

import com.alibaba.fastjson.JSON;
import com.smk.common.netty.constant.MsgType;
import com.smk.common.netty.message.HeartbeatResponsePacket;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.message.ResponseMsgPacket;
import com.smk.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description: Netty服务处理类，用于长连接
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/1 16:34
 * Copyright (c) , .
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RequestMsgPacket> {

    private Map<String, Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMsgPacket packet) throws Exception {
        if (packet.getMsgType().equals(MsgType.PING)) {
            log.info("收到客户端发来的心跳消息：{}", JsonUtil.object2Json(packet));
            //回应pong
            ctx.writeAndFlush(new HeartbeatResponsePacket(packet));
        } else {
// methodArguments该参数使用Gson会报stackoverflowError
//           log.info("收到客户端的业务消息：{}", JsonUtil.object2Json(packet));
            log.info("收到客户端的业务消息：{}", JSON.toJSONString(packet));
            ResponseMsgPacket response = new ResponseMsgPacket(packet);
            try {
                String interfaceName = packet.getInterfaceName();
                String methodName = packet.getMethodName();
                Class[] argClass = packet.getMethodArgumentClasses();
                Object[] args = packet.getMethodArguments();

                Object bean = serviceMap.get(interfaceName);
                Method method = bean.getClass().getMethod(methodName, argClass);
                Object result = method.invoke(bean, args);
                response.setErrorCode(200L);
                response.setMessage("Success");
                response.setPayload(result);
            } catch (Exception e) {
                log.error("解析处理 netty 客户端参数出错", e);
                response.setErrorCode(500L);
                //兼容空指针等情况
                response.setMessage(StringUtils.isEmpty(e.getMessage()) ? e.getCause().toString() : e.getMessage());
                response.setPayload(null);
            }
            //客户端关闭后，服务端要 addListener(ChannelFutureListener.CLOSE)
            log.info("发送响应报文:{}", JSON.toJSONString(response));

            //发完不关闭连接
            ctx.writeAndFlush(response);

        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
            log.warn("Channel idle in last {} seconds, close it", 60);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
