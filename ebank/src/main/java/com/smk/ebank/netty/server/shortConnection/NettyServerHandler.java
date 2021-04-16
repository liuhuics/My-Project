package com.smk.ebank.netty.server.shortConnection;

import com.alibaba.fastjson.JSON;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.message.ResponseMsgPacket;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description: Netty服务处理类，用于短连接
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/1 16:34
 * Copyright (c) , .
 */
@Slf4j
@ChannelHandler.Sharable
//一定要加Shareble注解，因为NettyServer类里使用它时有final标识符
public class NettyServerHandler extends SimpleChannelInboundHandler<RequestMsgPacket> {


    private Map<String, Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMsgPacket packet) throws Exception {
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

        //发完即关闭连接，短连接，或者通过 channelReadComplete 方法来添加关闭监听，不然前端一直阻塞
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);


    }

/*    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }*/

}
