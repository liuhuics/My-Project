package com.smk.admin.controller;


import com.smk.admin.constant.SysConstant;
import com.smk.admin.service.SysPermissionService;
import com.smk.admin.vo.req.PageReq;
import com.smk.admin.vo.resp.TreeNodeVO;
import com.smk.common.annotation.BizLog;
import com.smk.common.model.SysPermission;
import com.smk.common.model.SysUser;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.common.vo.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description: 权限管理
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/10 11:21
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
@Controller
@RequestMapping("/permission")
@Slf4j
public class PermissionController {


    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 跳到权限管理
     *
     * @return
     */
    @GetMapping("/permitManage")
    public String permissionManage() {
        log.info("进入权限管理");
        return "/permission/permissionManage";
    }


    /**
     * 获取权限菜单列表
     *
     * @param pageReq
     * @return
     */
    @PostMapping("/getPermitList")
    @BizLog("获取权限菜单列表")
    @ResponseBody
    public PageResponse getPermitList(PageReq pageReq) {
        return sysPermissionService.getPermissionList(pageReq);
    }


    /**
     * 获取第一级权限菜单列表，用于添加权限或修改权限用
     *
     * @return
     */
    @GetMapping("/getParentPermitList")
    @BizLog("获取第一级权限菜单列表")
    @ResponseBody
    public List<SysPermission> getParentPermitList() {
        return sysPermissionService.getParentPermissionList();
    }


    /**
     * 获取所有子权限
     *
     * @return
     */
    @GetMapping("/getAllChildPermits")
    @ResponseBody
    @BizLog("获取所有子权限")
    public List<SysPermission> getAllChildPermits() {
        return sysPermissionService.getAllChildPermits();
    }

    /**
     * 获取某角色下的所有子权限
     *
     * @return
     */
    @PostMapping("/getChildPermitsForRole")
    @ResponseBody
    @BizLog("获取某角色下所有子权限")
    public List<SysPermission> getChildPermitsForRole(Integer roleId) {
        return sysPermissionService.getAllChildPermitsforRole(roleId);
    }


    /**
     * 新增或更新权限
     *
     * @param permission
     * @return
     */
    @PostMapping("/saveOrUpdatePermit")
    @ResponseBody
    @RequiresPermissions("permit:edit")
    @BizLog("新增或更新权限")
    public BaseResult saveOrUpdatePermit(SysPermission permission) {
        if (StringUtils.isEmpty(permission.getPermName()) || StringUtils.isEmpty(permission.getPermCode())) {
            return BaseResultBuilder.buildBadRequestResult("权限编码和权限名称均不可为空");
        }

        if (SysConstant.ROOT_TREE_ID != permission.getPid() && StringUtils.isEmpty(permission.getUrl())) {
            return BaseResultBuilder.buildBadRequestResult("非一级权限时，url必须要填写");
        }

        if (permission.getId() == null) {
            //新增权限
            return sysPermissionService.addPermission(permission);
        } else {
            //修改权限
            return sysPermissionService.updatePermission(permission);
        }
    }


    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @PostMapping("/delPermit")
    @ResponseBody
    @RequiresPermissions("permit:edit")
    @BizLog("删除权限")
    public BaseResult del(Integer id) {
        return sysPermissionService.delPermit(id);
    }


    /**
     * 获取当前登录用户的权限
     *
     * @return
     */
    @GetMapping("/getUserPermits")
    @ResponseBody
    @BizLog("获取当前登录用户权限")
    public List<TreeNodeVO> getUserPermits() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        return sysPermissionService.getUserPerms(user);
    }

}
