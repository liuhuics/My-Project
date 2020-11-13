package com.smk.schedule.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description:redis 相关配置类
 * @Project: smkLoan
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/15 15:26
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Configuration
public class RedisConfig {

    @Autowired(required = false)
    private RedisConnectionFactory factory; //可启用redis注入或不启用

    /**
     * 定义 StringRedisTemplate ，指定序列化和反序列化的处理类
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        if (factory == null) {
            return null;
        }
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        //序列化值时使用此序列化方法
        template.setValueSerializer(jackson2JsonRedisSerializer);

        //key使用 StringRedisSerializer，不然在redis客户端查看时，key前面会有一些前缀，这是因为默认的序列化工具-JdkSerializationRedisSerializer序列化的结果
        RedisSerializer redisSerializer = new StringRedisSerializer();
        template.setKeySerializer(redisSerializer);

        return template;
    }

}
