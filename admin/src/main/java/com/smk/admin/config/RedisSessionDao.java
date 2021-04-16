package com.smk.admin.config;

import io.netty.util.concurrent.FastThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: session存储在redis中
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/13 11:38
 * Copyright (c) 2020
 */
@Slf4j
@Service
public class RedisSessionDao extends EnterpriseCacheSessionDAO {

    // session 在redis过期时间是30分钟30*60
    private static int expireTime = 1800;

    private String session_key = "admin";

    /**
     * 本地缓存超时时间 5s
     */
    private final long sessionInMemoryTimeout = 5 * 1000;

//    private RScoredSortedSet<String> sessionKeys;

    private static final FastThreadLocal<Map<Serializable, Session>> SESSIONS_IN_THREAD = new FastThreadLocal<>();

    private Cache cache() {
        Cache<Object, Object> cache = getCacheManager().getCache(session_key);
        return cache;
    }

//    @PostConstruct
//    public void init() {
//        this.sessionKeys = RedisUtil.getScoredSortedSet(cache()., StringCodec.INSTANCE);
//
//    }

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        if (session == null || session.getId() == null) {
            throw new UnknownSessionException("session or session id is null");
        }
        log.debug("创建session:{}", session.getId());
        cache().put(sessionId, session);

        //当前时间+超时时间
//        long time =
//                LocalDateTime.now().plusNanos(session.getTimeout() * 1000 * 1000).atZone(ZoneId.systemDefault())
// .toInstant().toEpochMilli();
//        if (sessionKeys == null) {
////            sessionKeys = RedisUtil.getScoredSortedSet(prefix, StringCodec.INSTANCE);
//            sessionKeys = RedisUtil.getScoredSortedSet(session_key);
//        }
//        sessionKeys.add(time, key);
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.warn("session id is null");
            return null;
        }
        log.debug("获取session:{}", sessionId);

        Session session = getSessionFromThreadLocal(sessionId);
        if (session == null) {
            session = (Session) cache().get(sessionId);
        }
        setSessionToThreadLocal(sessionId, session);
        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {

        log.debug("获取session:{}", session.getId());
        cache().put(session.getId(), session);
        setSessionToThreadLocal(session.getId(), session);
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
        log.debug("删除session:{}", session.getId());
//        sessionKeys.remove(key);
        cache().remove(session.getId());
    }

    /*@Override
    public Collection<Session> getActiveSessions() {

        // 获取存活的session
//        List<ScoredEntry<String>> keys = (List<ScoredEntry<String>>) sessionKeys
//                .entryRange(System.currentTimeMillis(), false, Double.MAX_VALUE, true);

        Iterable<String> keys = cache().keys();
        List<Session> values = new ArrayList<>();
        for (String key : keys) {
            Session session = (Session) RedisUtil.getBucket(key).get();
            if (session != null) {
                values.add(session);
            }
        }
        return Collections.unmodifiableList(values);

    }*/

    private void setSessionToThreadLocal(Serializable sessionId, Session s) {
        if (s == null) {
            return;
        }
        Map<Serializable, Session> sessionMap = SESSIONS_IN_THREAD.get();
        if (sessionMap == null) {
            sessionMap = new HashMap<>(4);
            SESSIONS_IN_THREAD.set(sessionMap);
        }
        sessionMap.put(sessionId, s);
    }

    private Session getSessionFromThreadLocal(Serializable sessionId) {
        Map<Serializable, Session> sessionMap = SESSIONS_IN_THREAD.get();
        if (sessionMap == null || sessionMap.get(sessionId) == null) {
            return null;
        }
        Session session = sessionMap.get(sessionId);

        long duration = System.currentTimeMillis() - session.getLastAccessTime().getTime();
        if (duration >= sessionInMemoryTimeout) {
            sessionMap.remove(sessionId);
        }

        return null;
    }


}

