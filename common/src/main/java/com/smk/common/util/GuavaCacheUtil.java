package com.smk.common.util;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/10 15:29
 * Copyright (c) 2020
 */
public class GuavaCacheUtil {

    /**
     * 存放角色、权限等不经常变动的数据
     */
    static Cache<String, String> basicParamcache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(30, TimeUnit.DAYS)
            .build();

    public static void put(String key, String value) {
        basicParamcache.put(key, value);
    }

    public static String get(String key) {
        return basicParamcache.getIfPresent(key);
    }


    public static void clearCache(String key) {
        basicParamcache.invalidate(key);
    }


}
