package com.smk.ebank.controller;

import com.smk.ebank.util.RedisLockUtil;
import com.smk.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/31 12:10
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@RestController
@Slf4j
@RequestMapping("/redisson")
public class RedissionExampleController {

    @Autowired
    RedissonClient redissonClient;


    /**
     * 锁测试共享变量
     */
    private Integer lockCount = 10;

    /**
     * 无锁测试共享变量
     */
    private Integer count = 10;

    private AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 模拟线程数
     */
    private static int threadNum = 10;

    @GetMapping("get")
    public String test1(String key1, String key2) {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet("mySortedSet");
        set.add(10, "1");
        set.add(20, "2");
        set.add(30, "3");

        RScoredSortedSet<String> set2 = RedisUtil.getScoredSortedSet("admin:session:");
        set2.add(10, "1");

        return "suc";
    }

    @GetMapping("increment")
    public String increment() {
        RAtomicLong rAtomicLong = RedisUtil.getAtomicLong("atom");
        long result = rAtomicLong.get();
        log.info("result：" + result);
        return "suc";

    }


    /**
     * 模拟并发测试加锁和不加锁
     *
     * @return
     */
    @GetMapping("/lock")
    public String lock() {
        // 计数器
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < threadNum; i++) {
            MyRunnable myRunnable = new MyRunnable(countDownLatch);
            Thread myThread = new Thread(myRunnable);
            myThread.start();
        }
        // 释放所有线程
        countDownLatch.countDown();
        return "suc";
    }

    /**
     * 加锁测试
     */
    private void testLockCount() {
        String lockKey = "lock-test";
        try {
            // 加锁，设置超时时间2s
            RedisLockUtil.lock(lockKey, TimeUnit.SECONDS, 2);
            lockCount--;
            log.info("lockCount值：" + lockCount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 释放锁
            RedisLockUtil.unlock(lockKey);
        }
    }

    private void testRedisIncrement() {
        RAtomicLong rAtomicLong = RedisUtil.getAtomicLong("atom");
        long result = rAtomicLong.incrementAndGet();
        log.info("atom result 值：" + result);
    }

    private void testIncrement() {
        Integer i = atomicInteger.getAndIncrement();
        log.info("result 值：" + i);
    }

    /**
     * 无锁测试
     */
    private void testCount() {
        count--;
        //不加锁则是乱序的
        log.info("count值：" + count);
    }


    public class MyRunnable implements Runnable {
        /**
         * 计数器
         */
        final CountDownLatch countDownLatch;

        public MyRunnable(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                // 阻塞当前线程，直到计时器的值为0
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            testIncrement();

            testRedisIncrement();

            // 无锁操作
            testCount();
            // 加锁操作
            testLockCount();
        }

    }
}
