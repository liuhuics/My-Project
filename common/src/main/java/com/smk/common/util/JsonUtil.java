package com.smk.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @Description: json 转换用工具类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/19 11:11
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
public class JsonUtil {

    private static Gson gson = new GsonBuilder()
            // 不导出实体中没有用 @Expose 注解的属性
            //            .excludeFieldsWithoutExposeAnnotation()
            // 时间格式
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    /**
     * json字符串转java对象
     *
     * @param json
     * @param clz
     * @return
     */
    public static <T> T json2Object(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    /**
     * java 对象转 json字符串
     *
     * @param object
     * @return
     */
    public static <T> String object2Json(T object) {
        return gson.toJson(object);
    }

    public static void main(String[] args) {

    }
}
