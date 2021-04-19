package com.smk.business.netty.client.longconnection;

import com.smk.common.netty.message.RequestMsgPacket;
import com.smk.common.netty.message.ResponseMsgPacket;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * RPCFuture for async RPC call
 * Created by luxiaoxun on 2016-03-15.
 */
@Slf4j
public class RpcFuture implements Future<Object> {

    private Sync sync;
    private RequestMsgPacket request;
    private ResponseMsgPacket response;
    private long startTime;
    private long responseTimeThreshold = 5000;

    public RpcFuture(RequestMsgPacket request) {
        this.sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() {
        sync.acquire(1);
        if (this.response != null) {
            return this.response;
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException {
        boolean success = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        if (success) {
            if (this.response != null) {
                return this.response;
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request serialNum: " + this.request.getSerialNumber()
                    + ". Request interface name: " + this.request.getInterfaceName()
                    + ". Request method: " + this.request.getMethodName());
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(ResponseMsgPacket reponse) {
        this.response = reponse;
        sync.release(1);
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            log.warn("Service response time is too slow. Request serialNum = " + reponse.getSerialNumber() + ". " +
                    "Response " +
                    "Time = " + responseTime + "ms");

        }
    }


    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;

        //future status
        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        protected boolean isDone() {
            return getState() == done;
        }
    }
}
