package com.smk.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.smk.admin.constant.SysConstant;
import com.smk.admin.service.SysUserService;
import com.smk.admin.util.RegrexUtil;
import com.smk.admin.util.ShiroUtil;
import com.smk.admin.vo.req.SysUserReq;
import com.smk.common.dao.SysUserMapper;
import com.smk.common.model.SysUser;
import com.smk.common.util.DateUtil;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.common.vo.admin.SysUserResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/7 14:41
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findValidUserByUserName(String userName) {
        return sysUserMapper.getValidUserByUserName(userName);
    }


    @Override
    public List<SysUserResp> getUserList(SysUserReq userSearch) {
        SysUser sysUser = new SysUser();
        if (StringUtils.isNotEmpty(userSearch.getSysUserName())) {
            sysUser.setUserName(userSearch.getSysUserName());
        }
        if (StringUtils.isNotEmpty(userSearch.getUserPhone())) {
            sysUser.setPhone(userSearch.getUserPhone());
        }
        PageHelper.startPage(userSearch.getPageNum(), userSearch.getPageSize());
        List<SysUserResp> users = sysUserMapper.getUsers(sysUser);


        /*PageSerializable<SysUserResp> pageInfo = PageHelper.startPage(userSearch.getPageNum(), userSearch
 .getPageSize())
                .doSelectPageSerializable(() ->

                    sysUserMapper.getUsers(sysUser)
                );*/


        return users;
    }


    @Override
    public BaseResult addUser(SysUser user) {

        String username = user.getUserName();
        SysUser old = sysUserMapper.getByUserName(username);
        if (old != null) {
            return BaseResultBuilder.buildBadRequestResult("用户名已存在！");
        }
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone) && !RegrexUtil.isMobile(phone)) {
            return BaseResultBuilder.buildBadRequestResult("手机号不规范！");
        }

        if (user.getUserPwd() == null) {
            String password = ShiroUtil.md5(username, "123456");
            user.setUserPwd(password);
        } else {
            String password = ShiroUtil.md5(username, user.getUserPwd());
            user.setUserPwd(password);
        }
        user.setCreateTime(DateUtil.getCurrentDate());
        user.setStatus(SysConstant.STATUS_VALID);

        sysUserMapper.insertSelective(user);

        return BaseResultBuilder.buildSuccessResult();
    }


    @Override
    public BaseResult updateUser(SysUser user) {

        String username = user.getUserName();
        int id = user.getId();
        SysUser old = sysUserMapper.getByUserName(username);
        if (old != null && old.getId() != id) {
            return BaseResultBuilder.buildBadRequestResult("用户名已存在！");
        }

        if (user.getUserPwd() != null) {
            String password = ShiroUtil.md5(username, user.getUserPwd());
            user.setUserPwd(password);
        }

        user.setUpdateTime(DateUtil.getCurrentDate());
        sysUserMapper.updateByPrimaryKeySelective(user);
        return BaseResultBuilder.buildSuccessResult();
    }


    @Override
    public SysUser getUserById(Integer id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }


    @Override
    public void updateUserStatus(Integer id, String status) {
        SysUser sysUser = new SysUser();
        sysUser.setStatus(status);
        sysUser.setId(id);
        sysUser.setUpdateTime(DateUtil.getCurrentDate());
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }


    @Override
    public void updatePwd(String userName, String password) {
        password = ShiroUtil.md5(userName, password);
        sysUserMapper.updatePwdByUserName(userName, password);
    }

}
