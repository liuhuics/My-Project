package com.smk.admin.config;

import com.smk.admin.constant.SysConstant;
import com.smk.admin.service.SysRoleService;
import com.smk.admin.service.SysUserService;
import com.smk.common.model.SysRole;
import com.smk.common.model.SysUser;
import com.smk.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @Description: 自定义realm
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/10 11:39
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;


    /**
     * 鉴权
     *
     * @param arg0
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

        log.info("授予角色和权限");

        // 添加权限 和 角色信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 获取当前登陆用户
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();

        // 普通用户，查询用户的角色，根据角色查询权限
        SysRole role = sysRoleService.getById(user.getRoleId());


        if (SysConstant.ADMIN_ROLE.equals(role.getAdmin())) {
            // 超级管理员，添加所有角色、添加所有权限
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
        } else {

            Set<String> permissions = sysRoleService.getPermissionsByRoleId(role.getId());
            // 角色对应的权限数据
            authorizationInfo.addRole(role.getRoleCode());
            // 授权角色下所有权限
            authorizationInfo.addStringPermissions(permissions);
        }
        return authorizationInfo;
    }


    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //UsernamePasswordToken用于存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        log.info("用户登录认证：验证当前Subject时获取到token为：" + JsonUtil.object2Json(token));
        String username = token.getUsername();

        // 调用数据层
        SysUser sysUser = sysUserService.findValidUserByUserName(username);
        log.debug("用户登录认证！用户信息user：" + sysUser);

        if (sysUser == null) {
            // 用户不存在
            return null;
        }
        // 返回密码
        return new SimpleAuthenticationInfo(sysUser, sysUser.getUserPwd(), ByteSource.Util.bytes(username), getName());
    }

}
