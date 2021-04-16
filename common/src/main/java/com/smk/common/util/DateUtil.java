package com.smk.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 尽量用commons-lang3包下的DateFormatUtils、DateUtils类，避免重复造轮子
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/7 17:39
 * Copyright (c) 2019
 */
public class DateUtil {

    public static final String FULL_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String FULL_TIME_FORMAT_SHORT = "yyyyMMdd HH:mm:ss";


    public static final String FULL_DAY_FORMAT = "yyyy-MM-dd";

    public static final String FULL_SIMPLE_DAY_FORMAT = "yyyyMMdd";

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date getDateAfterNow(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();

    }

    public static String getDate8(Date date) {
        SimpleDateFormat date8fmtStr = new SimpleDateFormat(FULL_SIMPLE_DAY_FORMAT);
        return date8fmtStr.format(date);
    }

    /**
     * 获取没有是分秒的Date
     *
     * @return
     */
    public static Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


}
