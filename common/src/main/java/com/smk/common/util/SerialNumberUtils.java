package com.smk.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/26 10:49
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Slf4j
public class SerialNumberUtils {
    private final long workerId;
    private final static long twepoch = 1288834974657L;
    private long sequence = 0L;
    private final static long workerIdBits = 4L;
    public final static long maxWorkerId = -1L ^ -1L << workerIdBits; //15
    private final static long sequenceBits = 10L;

    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    public final static long sequenceMask = -1L ^ -1L << sequenceBits;//1023
    private long lastTimestamp = -1L;

    private final static String ARG_NAME = "workerId";

    private static SerialNumberUtils worker2;

    static {
        String workId = System.getProperty(ARG_NAME);
        if (StringUtils.isEmpty(workId)) {
            worker2 = new SerialNumberUtils(10);
        } else {
            worker2 = new SerialNumberUtils(Integer.parseInt(workId.trim()));
        }
    }


    /**
     * 获取序列号
     *
     * @return
     */
    public static String getSeriaNumber() {
        return "NO" + worker2.nextId();
    }


    private SerialNumberUtils(final long workerId) {
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "worker Id can't be greater than %d or less than 0",
                    this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    private synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & this.sequenceMask;
            if (this.sequence == 0) {
                System.out.println("###########" + sequenceMask);
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(
                        String.format(
                                "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                                this.lastTimestamp - timestamp));
            } catch (Exception e) {
                log.error("获取序列号时出错", e);
            }
        }

        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift))
                | (this.workerId << this.workerIdShift) | (this.sequence);
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }


    public static void main(String[] arugs) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getSeriaNumber());
        }
    }
}
