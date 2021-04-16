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
 * @Description: Netty 服务，用于短连接
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/1 16:38
 * Copyright (c) , .
 */
//@Component
@Slf4j
public class NettyServer implements ApplicationContextAware {
    /**
     * boss 线程组用于处理连接工作
     */
    private EventLoopGroup boss = new NioEventLoopGroup(2);
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

                    //服务端接收连接的队列长度,对应TCP/IP协议listen函数中backlog参数。如果队列已满，客户端连接将被拒绝。
                    // windwos中默认是200，其他操作系统则为128
                    .option(ChannelOption.SO_BACKLOG, 1024)

                    //设置TCP长连接,即开启tcp底层心跳机制。
                    // 默认的心跳间隔是两个小时，即如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文，
                    .childOption(ChannelOption.SO_KEEPALIVE, true)

                    //该参数的含义是是否将小的数据包包装成更大的帧进行传送，true表示不要，即有数据时就发立刻送，也是netty默认值。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //设置子通道流水线
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
            //通过sync()同步方法阻塞直到绑定成功
            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                log.info("启动 Netty Server");
            }
            future.channel().closeFuture().sync();//获取Channel的CloseFuture,并阻塞当前线程直到通道完成

        } catch (Exception e) {
            work.shutdownGracefully().sync();
            boss.shutdownGracefully().sync();
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
