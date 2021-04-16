package com.smk.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/31 12:10
 * Copyright (c) 2019
 */
@RestController
@Slf4j
@RequestMapping("/ssl")
public class SSLExampleController {

    @Autowired
    private RestTemplate sslRestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public String test() {

        return "hello";
    }

    @GetMapping("/test1")
    public String test1() {
//        String result = sslRestTemplate.getForObject("https://localhost:8443/loan/business/ssl/test", String.class);
        String result = restTemplate.getForObject("http://localhost:18089/loan/business/ssl/test", String.class);
        log.info("ssl result:", result);
        return "suc";
    }
}
