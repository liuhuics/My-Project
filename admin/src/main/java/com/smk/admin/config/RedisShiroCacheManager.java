package com.smk.admin.config;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/13 13:09
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
public class RedisShiroCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        Cache cache = caches.get(s);

        if (cache == null) {
            cache = new ShiroCache<>(s);
            caches.put(s, cache);
        }
        return cache;
    }

}
