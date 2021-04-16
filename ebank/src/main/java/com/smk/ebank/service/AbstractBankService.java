package com.smk.ebank.service;

import com.smk.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 发送银行请求通用类
 * @Project: smkLoan
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/18 14:20
 * Copyright (c) 2019
 */
@Slf4j
public abstract class AbstractBankService<Req, Resp> {

    public static final String URL_PREFIX_HTTPS = "https://";
    public static final String URL_PREFIX_HTTP = "http://";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate sslRestTemplate;

    /**
     * 获取请求地址
     *
     * @return
     */
    public abstract String getRequestUrl();

    /**
     * 对请求参数的前置处理，如判空等操作
     *
     * @param req
     */
    private void preHandle(Req req) {
        if (req == null) {
            throw new BizException("请求内容不可为null");
        }
    }

    /**
     * 调用第三方接口
     *
     * @param req
     * @return
     */
    public Resp handleRequest(Req req) {
        preHandle(req);
        String url = getRequestUrl();
        if (StringUtils.isEmpty(url)) {
            throw new BizException("请求地址不可为空！");
        }
        if (!url.toLowerCase().startsWith(URL_PREFIX_HTTP) && !url.toLowerCase().startsWith(URL_PREFIX_HTTPS)) {
            throw new BizException("请求地址前缀不是http或https");
        }
        log.info("请求地址：{}", url);
        String respResult;
        try {
            respResult = execReq(url, req);
        } catch (Exception e) {
            log.error("发送请求时出错！", e);
            throw new BizException("发送请求时出错！" + e.getMessage());
        }
        Resp resp;
        try {
            resp = postHandle(respResult);
        } catch (Exception e) {
            log.error("转换响应内容时出错！", e);
            throw new BizException("转换响应内容时出错！" + e.getMessage());
        }
        return resp;
    }

    /**
     * 调用第三方
     *
     * @param url
     * @param req
     * @return
     */
    private String execReq(String url, Req req) {
        if (url.toLowerCase().startsWith(URL_PREFIX_HTTPS)) {
            return sslRestTemplate.postForObject(url, req, String.class);
        } else if (url.toLowerCase().startsWith(URL_PREFIX_HTTP)) {
            return restTemplate.postForObject(url, req, String.class);
        }
        return null;
    }


    /**
     * 对响应内容的后置处理，如转成javaBean等
     *
     * @param responseResult
     * @return
     */
    public abstract Resp postHandle(String responseResult) throws Exception;
}
