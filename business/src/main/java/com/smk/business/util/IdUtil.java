package com.smk.business.util;

import com.smk.common.util.DateUtil;
import com.smk.common.util.RedisKeyConstant;
import com.smk.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.redisson.api.RAtomicLong;

/**
 * @Description: id 相关工具类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/3/9 11:35
 * Copyright (c) 2020
 */
public class IdUtil {


    /**
     * 获取每天从1开始的编号，格式：bankType+当天8位的日期+六位的自增数字，如lh20200309000001
     *
     * @param bankType 业务类型
     * @return
     */
    public static String generateApplyId(String bankType) {
        String today = DateFormatUtils.format(DateUtil.getCurrentDate(), DateUtil.FULL_SIMPLE_DAY_FORMAT);

        String key = bankType + today;
        RAtomicLong rAtomicLong = RedisUtil.getAtomicLongExpiryAfterDays(RedisKeyConstant.ORDER_NO_PREFIX + key, 1);

        return key + StringUtils.substring(String.format("%06d", rAtomicLong.incrementAndGet()), 0, 6);
    }

}
