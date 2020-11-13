package com.smk.admin.config;

import com.smk.common.util.RedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class ShiroCache<K, V> implements Cache<K, V> {

    /**
     * session缓存的key前缀
     */
    public final static String REDIS_SHIRO_CACHE = "shiro-cache:";

    /**
     * session在redis中的默认过期时间 30 min
     */
    private final static long DEFAULT_TIME_OUT = 30 * 60;

    private String cacheKey;

    @SuppressWarnings("rawtypes")
    public ShiroCache(String name) {
        this.cacheKey = REDIS_SHIRO_CACHE + name + ":";
    }

    @Override
    public V get(K key) throws CacheException {
        return (V) RedisUtil.getBucket((String) getCacheKey(key)).get();
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V old = get(key);
        RedisUtil.getBucket((String) getCacheKey(key)).set(value, DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        return old;
    }

    @Override
    public V remove(K key) throws CacheException {
        V old = get(key);
        RedisUtil.getBucket((String) getCacheKey(key)).delete();
        return old;
    }

    @Override
    public void clear() throws CacheException {
        RedisUtil.deleteKeys((String) getCacheKey("*"));
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) RedisUtil.getKeys((String) getCacheKey("*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    private K getCacheKey(Object k) {
        return (K) (this.cacheKey + k);
    }
}