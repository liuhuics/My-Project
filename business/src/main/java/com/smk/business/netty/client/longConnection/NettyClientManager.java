package com.smk.business.netty.client.longConnection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/14 15:35
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Component
@Slf4j
public class NettyClientManager {

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8,
            600L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

    private Map<ServerAdress, NettyClientHandler> connectedServerNodes = new ConcurrentHashMap<>();
    List<ServerAdress> serverAdresses = new ArrayList<>();

    private AtomicInteger roundRobin = new AtomicInteger(0);

    private NettyClientManager() {
    }

    private static class SingletonHolder {
        private static final NettyClientManager instance = new NettyClientManager();
    }

    public static NettyClientManager getInstance() {
        return NettyClientManager.SingletonHolder.instance;
    }

    /**
     * 配置的远端地址：如
     */
    @Value("${netty.server.addresses:}")
    private String addresses;

    /**
     * 逐个连接每个远端地址
     */
    @PostConstruct
    public void connectServerNodes() {
        List<String> addressList = Arrays.asList(addresses.split(","));

        serverAdresses = addressList.stream().map((addr) -> {
            ServerAdress serverAdress = new ServerAdress();
            serverAdress.setHost(addr.split(":")[0]);
            serverAdress.setPort(Integer.parseInt(addr.split(":")[1]));
            return serverAdress;
        }).collect(Collectors.toList());

        serverAdresses.forEach((serverAdresse) -> connectServerNode(serverAdresse));
    }

    /**
     * 连接远端地址
     *
     * @param serverAdress
     */
    private void connectServerNode(ServerAdress serverAdress) {

        log.info("New service node, host: {}, port: {}", serverAdress.getHost(), serverAdress.getPort());
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(serverAdress.getHost(),
                serverAdress.getPort());
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Bootstrap b = new Bootstrap();
                b.group(eventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new NettyClientInitializer());

                ChannelFuture channelFuture = b.connect(inetSocketAddress);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            log.info("Successfully connect to remote server, remote peer = " + inetSocketAddress);
                            NettyClientHandler handler =
                                    channelFuture.channel().pipeline().get(NettyClientHandler.class);
                            connectedServerNodes.put(serverAdress, handler);
                        } else {
                            log.error("Can not connect to remote server, remote peer = " + inetSocketAddress);
                        }
                    }
                });
            }
        });
    }

    /**
     * 选择一个handler
     *
     * @return
     * @throws Exception
     */
    public NettyClientHandler chooseHandler() throws Exception {
        ServerAdress serverAdress = route();
        NettyClientHandler handler = connectedServerNodes.get(serverAdress);
        if (handler != null) {
            return handler;
        } else {
            throw new Exception("Can not get available connection");
        }
    }

    /**
     * 随机选择一个
     *
     * @return
     */
    private ServerAdress route() {
        int size = serverAdresses.size();
        // Round robin
        int index = (roundRobin.getAndAdd(1) + size) % size;
        return serverAdresses.get(index);
    }

    public void stop() {
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }
}
