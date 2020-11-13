package com.smk.admin.controller;

import com.smk.admin.service.SysUserService;
import com.smk.admin.vo.req.LoginVO;
import com.smk.admin.vo.req.SysUserReq;
import com.smk.common.annotation.BizLog;
import com.smk.common.model.SysUser;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.common.vo.CommonResponse;
import com.smk.common.vo.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 系统用户管理
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/10 11:12
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
@Controller
@RequestMapping("/sysUser")
@Slf4j
public class SysUserController {


    @Autowired
    private SysUserService sysUserService;


    /**
     * 登入系统
     *
     * @param loginVO
     * @param session
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public BaseResult login(LoginVO loginVO) {

        // 使用 shiro 进行登录
        Subject subject = SecurityUtils.getSubject();

        String userName = loginVO.getUsername().trim();
        String password = loginVO.getPassword().trim();

        boolean remember = false;
        if (StringUtils.equals("on", loginVO.getRememberMe())) {
            remember = true;
        }

        //获取token
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password,
                remember);

        try {
            subject.login(token);
            // 登录成功
            SysUser user = (SysUser) subject.getPrincipal();

//            session.setAttribute("user", user.getUserName());

            log.info(user.getUserName() + "登录成功");

        } catch (UnknownAccountException e) {
            log.error(userName + "帐号不存在", e);
            return BaseResultBuilder.buildServerErrorResult(userName + "帐号不存在");
        } catch (DisabledAccountException e) {
            log.error(userName + "帐号异常", e);
            return BaseResultBuilder.buildServerErrorResult(userName + "帐号异常");
        } catch (AuthenticationException e) {
            log.error(userName + "密码错误", e);
            return BaseResultBuilder.buildServerErrorResult(userName + "密码错误");
        }

        return new CommonResponse("/home");
    }


    /**
     * 跳到系统用户列表
     *
     * @return
     */
    @GetMapping("/userManage")
    public String userManage() {
        return "user/userManage";
    }

    /**
     * 修改用户密码
     *
     * @param pwd
     * @param rePwd
     * @return
     */
    @PostMapping("updatePwd")
    @ResponseBody
    @BizLog("修改用户密码")
    public BaseResult updatePwd(String pwd, String rePwd) {
        Map<String, Object> data = new HashMap();
        if (!StringUtils.equals(pwd, rePwd)) {
            return BaseResultBuilder.buildBadRequestResult("两次输入的密码不一致!");
        }
        //获取当前登陆的用户信息
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        sysUserService.updatePwd(user.getUserName(), pwd);
        return BaseResultBuilder.buildSuccessResult();
    }


    /**
     * 分页查询用户列表
     *
     * @param userSearch
     * @return
     */
    @PostMapping(value = "/getUserList")
    @BizLog("获取用户列表")
    @ResponseBody
    public PageResponse getUserList(SysUserReq userSearch) {
        PageResponse pageResponse = new PageResponse(sysUserService.getUserList(userSearch));

        return pageResponse;

    }


    /**
     * 新增或更新用户
     *
     * @param user
     * @return
     */
    @PostMapping("/saveOrUpdateUser")
    @ResponseBody
    @BizLog("新增或更新用户")
    @RequiresPermissions("user:edit")
    public BaseResult saveorUpdateUser(SysUser user) {
        if (user.getId() == null) {
            return sysUserService.addUser(user);
        } else {
            return sysUserService.updateUser(user);
        }
    }


    /**
     * 删除(软删除)/恢复 用户
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping(value = "/updateUserStatus")
    @ResponseBody
    @BizLog("删除/恢复用户！")
    @RequiresPermissions("user:edit")
    public BaseResult updateUserStatus(Integer id, String status) {
        sysUserService.updateUserStatus(id, status);
        return BaseResultBuilder.buildSuccessResult();
    }

}
