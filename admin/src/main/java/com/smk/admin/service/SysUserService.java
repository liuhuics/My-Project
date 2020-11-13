package com.smk.admin.service;

import com.smk.admin.vo.req.SysUserReq;
import com.smk.common.model.SysUser;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.admin.SysUserResp;

import java.util.List;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/7 14:39
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
public interface SysUserService {

    /**
     * 根据用户名称获取用户
     *
     * @param userName
     * @return
     */
    SysUser findValidUserByUserName(String userName);

    /**
     * 根据不同的查询条件查询用户列表
     *
     * @param sysUserReq
     * @return
     */
    List<SysUserResp> getUserList(SysUserReq sysUserReq);


    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    BaseResult addUser(SysUser user);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    BaseResult updateUser(SysUser user);


    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    SysUser getUserById(Integer id);


    /**
     * 更新用户的状态
     *
     * @param id
     * @param status
     */
    void updateUserStatus(Integer id, String status);

    /**
     * 更新用户密码
     *
     * @param userName 用户名
     * @param password 用户密码
     */
    void updatePwd(String userName, String password);

}
