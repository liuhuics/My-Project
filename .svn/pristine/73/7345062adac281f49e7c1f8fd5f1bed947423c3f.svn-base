package com.smk.business.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 分布式锁的工具类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/31 15:31
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Component
public class RedisLockUtil {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedisLockUtil.redissonClient = redissonClient;
    }


    /**
     * 长期持有锁，直接unlock
     *
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 持有锁的时长为 leaseTime，单位为 s
     *
     * @param lockKey
     * @param leaseTime
     * @return
     */
    public static RLock lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }


    /**
     * 持有锁的时长为指定时长
     *
     * @param lockKey
     * @param unit      时间单位
     * @param leaseTime 持有时长
     * @return
     */
    public static RLock lock(String lockKey, TimeUnit unit, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, unit);
        return lock;
    }


    /**
     * 尝试持有锁，尝试时长为 waitTime，持有最长时间为 leaseTime
     *
     * @param lockKey
     * @param unit
     * @param waitTime  尝试时长
     * @param leaseTime 持有最长时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock != null && lock.isLocked()) {
            lock.unlock();
        }
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    public static void unlock(RLock lock) {
        if (lock != null && lock.isLocked()) {
            lock.unlock();
        }
    }
}
