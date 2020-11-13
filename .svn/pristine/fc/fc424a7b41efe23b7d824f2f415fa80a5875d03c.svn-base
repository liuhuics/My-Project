package com.smk.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: LoginController
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 11:39
 */
@Controller
@Slf4j
public class IndexController {

    @RequestMapping("/")
    public String toLogin() {
        return "login";
    }

    /**
     * 定向登录页
     *
     * @return
     */
    @RequestMapping("/login")
    public String tologin() {
        return "login";
    }

    /**
     * 定向主页
     *
     * @return
     */
    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * 退出系统
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout() {
        log.info("退出系统");
        Subject subject = SecurityUtils.getSubject();
        subject.logout(); // shiro底层删除session的会话信息
        return "redirect:login";
    }

    @RequestMapping("/404")
    public String notFound() {
        return "404";
    }


}
