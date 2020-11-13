package com.smk.admin.service;

import com.smk.admin.vo.req.PageReq;
import com.smk.admin.vo.resp.TreeNodeVO;
import com.smk.common.model.SysPermission;
import com.smk.common.model.SysUser;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.PageResponse;

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
public interface SysPermissionService {


    /**
     * 增加权限
     *
     * @param sysPermission
     * @return
     */
    BaseResult addPermission(SysPermission sysPermission);

    /**
     * 更新权限
     *
     * @param sysPermission
     * @return
     */
    BaseResult updatePermission(SysPermission sysPermission);


    /**
     * 功能描述: 获取权限菜单列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/30 11:35
     */
    PageResponse getPermissionList(PageReq pageReq);


    /**
     * 获取所有一级权限
     *
     * @return
     */
    List<SysPermission> getParentPermissionList();


    /**
     * 获取所有最后一级权限
     *
     * @return
     */
    List<SysPermission> getAllChildPermits();

    /**
     * 获取某角色的所有最后一级权限
     *
     * @param roleId
     * @return
     */
    List<SysPermission> getAllChildPermitsforRole(Integer roleId);


    /**
     * 删除权限
     *
     * @param id 权限id
     * @return
     */
    BaseResult delPermit(Integer id);

    /**
     * 根据id获取权限
     *
     * @param id
     * @return
     */
    SysPermission getById(int id);


    /**
     * 获取用户的权限
     *
     * @param user
     * @return
     */
    List<TreeNodeVO> getUserPerms(SysUser user);
}
