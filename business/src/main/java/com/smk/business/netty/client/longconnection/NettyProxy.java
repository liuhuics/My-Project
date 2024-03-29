package com.smk.business.netty.client.longconnection;

import com.smk.common.exception.BizException;
import com.smk.common.netty.constant.MsgType;
import com.smk.common.netty.constant.NettyConstant;
import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.message.ResponseMsgPacket;
import com.smk.common.util.SerialNumberUtils;
import com.smk.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:动态代理类，用于长连接
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/31 17:52
 * Copyright (c) 2020
 */
@Component
@Slf4j
public class NettyProxy {

    private static ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                    Runtime.getRuntime().availableProcessors(),
                    600L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));

    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{interfaceClass}, (proxy, method, args) -> {
                    if (Object.class == method.getDeclaringClass()) {
                        String name = method.getName();
                        if ("equals".equals(name)) {
                            return proxy == args[0];
                        } else if ("hashCode".equals(name)) {
                            return System.identityHashCode(proxy);
                        } else if ("toString".equals(name)) {
                            return proxy.getClass().getName() + "@" +
                                    Integer.toHexString(System.identityHashCode(proxy)) +
                                    ", with InvocationHandler " + this;
                        } else {
                            throw new IllegalStateException(String.valueOf(method));
                        }
                    }

                    RequestMsgPacket requestMsgPacket = new RequestMsgPacket();
                    requestMsgPacket.setMagicNumber(NettyConstant.MAGIC_NUMBER);
                    requestMsgPacket.setVersion(NettyConstant.VERSION);
                    requestMsgPacket.setSerialNumber(SerialNumberUtils.getSeriaNumber());
                    requestMsgPacket.setMsgType(MsgType.REQUEST);
                    requestMsgPacket.setInterfaceName(interfaceClass.getName());
                    requestMsgPacket.setMethodName(method.getName());


                    Class[] para = method.getParameterTypes();
                    String[] s = new String[para.length];
                    for (int i = 0; i < para.length; i++) {
                        s[i] = para[i].getCanonicalName();
                    }
                    requestMsgPacket.setMethodArguments(args);
                    requestMsgPacket.setMethodArgumentClasses(para);
                    requestMsgPacket.setMethodReturnType(method.getReturnType());


                    //--长连接

                    //初始化RPC客户端
//                    NettyClientHandler client = NettyClientManager.getInstance().chooseHandler();
                    NettyClientHandler client =
                            ((NettyClientManager) SpringContextUtil.getBean("nettyClientManager")).chooseHandler();
                    // 设置参数
                    client.setRequestMsgPacket(requestMsgPacket);

                    long startTime = System.currentTimeMillis();

                    ResponseMsgPacket response = threadPoolExecutor.submit(client).get();
                    log.debug("send rpc request elapsed time: {}ms...", System.currentTimeMillis() - startTime);

                    if (response == null) {
                        throw new RuntimeException("response is null...");
                    }

                    //返回RPC响应结果
                    if (response.getErrorCode() != 200) {
                        throw new BizException(response.getMessage());
                    } else {
                        return response.getPayload();
                    }
                });
    }


}
