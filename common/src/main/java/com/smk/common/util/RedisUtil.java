package com.smk.common.util;

import org.redisson.api.*;
import org.redisson.client.codec.BaseCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/31 16:09
 * Copyright (c) 2019
 */
@Component
public class RedisUtil {


    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedisUtil.redissonClient = redissonClient;
    }


    /**
     * 获取字符串对象: FST codec is used by default
     *
     * @param objectName
     * @return
     */
    public static <T> RBucket<T> getBucket(String objectName) {
        RBucket<T> bucket = redissonClient.getBucket(objectName);
        return bucket;
    }


    /**
     * 获取符合一定格式的keys
     *
     * @param pattern ，如 session*
     * @return
     */
    public static Iterable<String> getKeys(String pattern) {
        RKeys keys = redissonClient.getKeys();
        return keys.getKeysByPattern(pattern);
    }

    /**
     * 删除符合一定格式的keys
     *
     * @param pattern
     * @return
     */
    public static long deleteKeys(String pattern) {
        RKeys keys = redissonClient.getKeys();
        return keys.deleteByPattern(pattern);
    }


    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public static <K, V> RMap<K, V> getMap(String objectName) {
        RMap<K, V> map = redissonClient.getMap(objectName);
        return map;
    }

    /**
     * 获取有序集合
     *
     * @param objectName
     * @return
     */
    public static <V> RSortedSet<V> getSortedSet(String objectName) {
        RSortedSet<V> sortedSet = redissonClient.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 获取有序集合
     *
     * @param objectName
     * @return
     */
//    public static <V> RScoredSortedSet<V> getScoredSortedSet(String objectName, BaseCodec codec) {
//        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(objectName, codec);
//        return sortedSet;
//    }

    public static <V> RScoredSortedSet<V> getScoredSortedSet(String objectName) {
        RScoredSortedSet<V> sortedSet = redissonClient.getScoredSortedSet(objectName);
        return sortedSet;
    }


    /**
     * 获取集合
     *
     * @param objectName
     * @return
     */
    public static <V> RSet<V> getSet(String objectName) {
        RSet<V> rSet = redissonClient.getSet(objectName);
        return rSet;
    }

    /**
     * 获取列表
     *
     * @param objectName
     * @return
     */
    public static <V> RList<V> getList(String objectName) {
        RList<V> rList = redissonClient.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     *
     * @param objectName
     * @return
     */
    public static <V> RQueue<V> getQueue(String objectName) {
        RQueue<V> rQueue = redissonClient.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取双端队列
     *
     * @param objectName
     * @return
     */
    public static <V> RDeque<V> getDeque(String objectName) {
        RDeque<V> rDeque = redissonClient.getDeque(objectName);
        return rDeque;
    }


    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public static RAtomicLong getAtomicLong(String objectName) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取记数锁
     *
     * @param objectName
     * @return
     */
    public static RCountDownLatch getCountDownLatch(String objectName) {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public static RTopic getTopic(String objectName) {
        RTopic rTopic = redissonClient.getTopic(objectName);
        return rTopic;
    }


    /**
     * 获取带失效时间的atomicLong类型
     *
     * @param key
     * @param timeToLive 失效时间，单位s
     * @return
     */
    public static RAtomicLong getAtomicLongWithExpiry(String key, long timeToLive) {
        RAtomicLong value = redissonClient.getAtomicLong(key);
        if (value.remainTimeToLive() < 0 && timeToLive > 0) {
            value.expire(timeToLive, TimeUnit.SECONDS);
        }
        return value;
    }

    /**
     * 获取atomicLong类型,days 天后过期
     *
     * @param key
     * @param days
     * @return
     */
    public static RAtomicLong getAtomicLongExpiryAfterDays(String key, int days) {
        RAtomicLong value = redissonClient.getAtomicLong(key);
        if (value.remainTimeToLive() < 0 && days > 0) {
            value.expireAt(DateUtil.getDateAfterNow(days));
        }
        return value;
    }


}
