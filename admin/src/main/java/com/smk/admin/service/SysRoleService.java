package com.smk.admin.service;

import com.smk.admin.vo.req.PageReq;
import com.smk.admin.vo.req.SysRoleReq;
import com.smk.common.model.SysRole;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.PageResponse;

import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/7 14:39
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
public interface SysRoleService {

    /**
     * 根据id获取角色
     *
     * @param id 角色id
     * @return
     */
    SysRole getById(Integer id);

    /**
     * 获取角色的权限
     *
     * @param id 角色id
     * @return
     */
    Set<String> getPermissionsByRoleId(Integer id);

    /**
     * 获取角色列表
     *
     * @param sysRole 限定条件
     * @return
     */
    List<SysRole> getRoles(SysRole sysRole);


    /**
     * 分页获取角色列表
     *
     * @param pageReq
     * @return
     */
    PageResponse getRolesByPage(PageReq pageReq);

    /**
     * 删除角色
     *
     * @param id
     */
    BaseResult delRole(Integer id);

    /**
     * 插入角色
     *
     * @param roleReq
     * @return
     */
    BaseResult addRole(SysRoleReq roleReq);

    /**
     * 更新角色
     *
     * @param roleReq
     * @return
     */
    BaseResult updateRole(SysRoleReq roleReq);
}
