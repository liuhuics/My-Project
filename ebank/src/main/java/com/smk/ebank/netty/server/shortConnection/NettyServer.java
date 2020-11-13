package com.smk.ebank.netty.server.shortConnection;

import com.smk.ebank.netty.config.RpcService;
import com.smk.ebank.netty.server.RequestMsgPacketDecoder;
import com.smk.ebank.netty.server.ResponseMsgPacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Netty 服务
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/1 16:38
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
@Component
@Slf4j
public class NettyServer implements ApplicationContextAware {
    /**
     * boss 线程组用于处理连接工作
     */
    private EventLoopGroup boss = new NioEventLoopGroup();
    /**
     * work 线程组用于数据处理
     */
    private EventLoopGroup work = new NioEventLoopGroup();
    @Value("${netty.port}")
    private Integer port;

    private Map<String, Object> serviceMap = new HashMap<>();

    /**
     * 启动Netty Server
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        try {
            final NettyServerHandler handler = new NettyServerHandler(serviceMap);
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    // 指定Channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))

                    //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .option(ChannelOption.SO_BACKLOG, 1024)

                    //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true)

                    //将小的数据包包装成更大的帧进行传送，提高网络的负载
                    .childOption(ChannelOption.TCP_NODELAY, true)

                    .childHandler(new ChannelInitializer<Channel>() {
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
                                    .addLast(new LengthFieldPrepender(4))
                                    .addLast(new RequestMsgPacketDecoder())
                                    .addLast(new ResponseMsgPacketEncoder())
                                    .addLast(handler);


                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                log.info("启动 Netty Server");
            }
            future.channel().closeFuture().sync();//获取Channel的CloseFuture,并阻塞当前线程直到它完成

        } catch (Exception e) {
            boss.shutdownGracefully().sync();
            work.shutdownGracefully().sync();
            log.info("关闭Netty");
        }

    }

    /**
     * 功能描述: 在这里将所有rpc服务类加载进内存
     *
     * @param:
     * @return:
     * @auther: liuhui
     * @date: 2020/11/11 0:10
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean : beans.values()) {
            Class<?> clazz = serviceBean.getClass();
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> inter : interfaces) {
                String interfaceName = inter.getName();
                log.info("加载服务类: {}", interfaceName);
                serviceMap.put(interfaceName, serviceBean);
            }
        }
        log.info("已加载全部服务接口:{}", serviceMap);
    }


}
