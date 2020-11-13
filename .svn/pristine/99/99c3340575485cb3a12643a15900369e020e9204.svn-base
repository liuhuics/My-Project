package com.smk.common.util;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/3 15:45
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Component
public class MessageUtil {

    /**
     * 获取某个code 默认locale下对应的值，如果没有该值，则返回code本身
     *
     * @param code
     * @return
     */
    public static String get(String code) {
        return SpringContextUtil.getApplicationContext().getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

    /**
     * 获取某个code 且某个locale下对应的值，如果没有该值，则返回code本身
     *
     * @param code
     * @param locale
     * @return
     */
    public static String getMessage(String code, Locale locale) {
        return SpringContextUtil.getApplicationContext().getMessage(code, null, code, locale);
    }

    /**
     * 获取某个code 默认locale下对应的值，如果没有该值，defaultMessage
     *
     * @param code
     * @param defaultMessage
     * @return
     */
    public static String getMessage(String code, String defaultMessage) {
        return SpringContextUtil.getApplicationContext()
                .getMessage(code, null, defaultMessage, LocaleContextHolder.getLocale());
    }

}
