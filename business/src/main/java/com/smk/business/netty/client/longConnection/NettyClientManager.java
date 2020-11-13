package com.smk.business.netty.client.longConnection;

import com.google.common.collect.Lists;
import com.smk.common.netty.constant.NettyConstant;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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

    @Value("${netty.server.address:localhost:9092}")
    private String address;

    private Map<ServerAdress, NettyClientHandler> connectedServerNodes = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();

    private Random random = new Random();
    private long waitTimeout = 5000;

    private static ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                    Runtime.getRuntime().availableProcessors(),
                    600L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

    private static class SingletonHolder {
        private static final NettyClientManager instance = new NettyClientManager();
    }

    public static NettyClientManager getInstance() {
        return SingletonHolder.instance;
    }

    @PostConstruct
    public void connectServers() {
        List<ServerAdress> serverAdresses = parseServerAddress();
        connectServerNodes(serverAdresses);
    }


    private void connectServerNodes(List<ServerAdress> serverAdresses) {
        for (ServerAdress serverAdress : serverAdresses) {
            final InetSocketAddress remotePeer = new InetSocketAddress(serverAdress.getHost(), serverAdress.getPort());
            threadPoolExecutor.submit(() -> {
                EventLoopGroup group = new NioEventLoopGroup();
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new NettyClientInitializer());

                ChannelFuture channelFuture = b.connect(remotePeer);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            log.info("Successfully connect to remote server, remote peer = " + remotePeer);
                            NettyClientHandler handler =
                                    channelFuture.channel().pipeline().get(NettyClientHandler.class);
                            connectedServerNodes.put(serverAdress, handler);
                            handler.setServerAdress(serverAdress);
                            //可以获取客户端了
                            signalAvailableHandler();
                        } else {
                            log.error("Can not connect to remote server, remote peer = " + remotePeer);
                        }
                    }
                });
            });
        }
    }

    public NettyClientHandler chooseHandler() throws Exception {
        int size = connectedServerNodes.values().size();
        while (size <= 0) {
            try {
                waitingForHandler();
                size = connectedServerNodes.values().size();
            } catch (InterruptedException e) {
                log.error("Waiting for available service is interrupted!", e);
            }
        }
        ServerAdress serverAddress = getRandomServerAddress(connectedServerNodes.keySet());
        NettyClientHandler handler = connectedServerNodes.get(serverAddress);
        if (handler != null) {
            return handler;
        } else {
            throw new Exception("Can not get available connection");
        }
    }

    private ServerAdress getRandomServerAddress(Set<ServerAdress> serverAdresses) {
        List<ServerAdress> serverAdressesCopy = Lists.newArrayList(serverAdresses);
        int size = serverAdressesCopy.size();
        // Random
        return serverAdressesCopy.get(random.nextInt(size));
    }

    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean waitingForHandler() throws InterruptedException {
        lock.lock();
        try {
            log.warn("Waiting for available service");
            return connected.await(this.waitTimeout, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    private List<ServerAdress> parseServerAddress() {
        String[] addressArr = this.address.split(",");
        List<ServerAdress> serverAdresses = new ArrayList<>(addressArr.length);
        for (String addr : addressArr) {
            int port = NettyConstant.DEFAULT_SERVER_PORT;
            String host = addr;
            int index = addr.lastIndexOf(':');
            if (index >= 0) {
                // otherwise : is at the end of the string, ignore
                if (index < addr.length() - 1) {
                    port = Integer.parseInt(host.substring(index + 1));
                }
                host = addr.substring(0, index);
            }
            serverAdresses.add(new ServerAdress(host, port));

        }
        return serverAdresses;
    }

    private class ConnectAction implements Runnable {

        @Override
        public void run() {

        }
    }
}
