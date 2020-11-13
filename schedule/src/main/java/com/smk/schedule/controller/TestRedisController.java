package com.smk.schedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Project: smkLoan
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/12 15:30
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@RestController
@RequestMapping("/testRedis")
public class TestRedisController {

    @Autowired(required = false)
    RedisTemplate<String, Object> redisTemplate;

    @GetMapping("set/{key}/{value}")
    public String set(@PathVariable("key") String key, @PathVariable("value") String value) {

        //注意这里的 key不能为null spring 内部有检验
        redisTemplate.opsForValue().set(key, value);
        return key + "," + value;
    }

    @GetMapping("get/{key}")
    public String get(@PathVariable("key") String key) {

        return "key=" + key + ",value=" + redisTemplate.opsForValue().get(key);
    }

    @GetMapping("setObject/{key}")
    public String setObject(@PathVariable("key") String key) {
        /*UserAccount userAccount = new UserAccount();
        userAccount.setUserId("1");
        userAccount.setUserCname("liuhui");

        redisTemplate.opsForValue().set(key, userAccount);*/
        return "success";
    }
}
