package com.smk.admin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/20 10:56
 * Copyright (c) 2020
 */
public class RegrexUtil {

    /**
     * 较验手机号是否合法
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
//        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|" +
//                "(19[8|9|2]))\\d{8}$");
        Pattern p = Pattern.compile("\\d{11}");
        Matcher m = p.matcher(mobile);
        return m.matches();

    }
}
