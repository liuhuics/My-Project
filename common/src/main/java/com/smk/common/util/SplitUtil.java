package com.smk.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * from copy hmls
 *
 * @author linpeng
 */
public class SplitUtil {

    private static Pattern pattern = Pattern.compile("^(([1-9]\\d*)|([0]))(\\.(\\d){0,2})?$");

    /**
     * 逗号连接
     *
     * @author Administrator
     */
    public static String split(String[] str) throws Exception {
        StringBuilder newStr = null;
        for (int i = 0; i < str.length; i++) {
            if (i == 0) {
                newStr = new StringBuilder(str[i]);
            } else {
                newStr.append(",").append(str[i]);
            }
        }
        return newStr.toString();
    }

    /**
     * 清理时间格式
     *
     * @author Administrator
     */
    public static String cleanString(String time) throws Exception {
        if (time == null || time.isEmpty()) {
            return "";
        }
        return time.replaceAll("[.][^.]+$", "");
    }

    /**
     * 逗号分隔
     *
     * @author Administrator
     */
    public static String[] toSplit(String str) throws Exception {
        return str.replace(",", "，").split("，");
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }
        return s;
    }

    /**
     * 清理格式
     *
     * @param i
     * @return
     */
    public static String cleanString(Integer i) {
        if (i == null) {
            return "";
        }
        return String.valueOf(i);
    }

    /**
     * 清理格式
     *
     * @param l
     * @return
     */
    public static String cleanString(Long l) {
        if (l == null) {
            return "";
        }
        return String.valueOf(l);
    }

    /**
     * 清理格式
     *
     * @param decimal
     * @return
     */
    public static String cleanString(BigDecimal decimal) {
        if (decimal == null) {
            return "0";
        }
        return subZeroAndDot(String.valueOf(decimal));
    }

    /**
     * Integer类型清理格式
     *
     * @param str
     * @return
     */
    public static Integer cleanInteger(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Decimal类型清理格式
     *
     * @param str
     * @return
     */
    public static BigDecimal cleanDecimal(String str) {
        BigDecimal returnAmt;
        try {
            Matcher matcher = pattern.matcher(str);
            boolean result = matcher.matches();
            if (result) {
                returnAmt = new BigDecimal(str);
            } else {
                returnAmt = BigDecimal.ZERO;
            }
        } catch (Exception e) {
            returnAmt = BigDecimal.ZERO;
        }
        return returnAmt;
    }

    /**
     * 用于授信日期 yyyy-MM-dd 转化 yyyyMMdd
     *
     * @param stringDate
     * @return
     */
    public static String formatDate10toDate8(String stringDate) {
        String returnDate;
        try {
            Date date = DateUtils.parseDate(stringDate, DateUtil.FULL_DAY_FORMAT);
            returnDate = DateUtil.getDate8(date);
        } catch (ParseException e) {
            return null;
        }
        return returnDate;
    }

    /**
     * str转long
     *
     * @param str
     * @return
     */
    public static Long formatLong(String str) {
        if (StringUtils.isBlank(str)) {
            return 0L;
        }
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * str转short
     *
     * @param str
     * @return
     */
    public static short formatShort(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }
        try {
            return Short.parseShort(str);
        } catch (Exception e) {
            return 0;
        }
    }
}
