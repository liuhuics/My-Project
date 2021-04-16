package com.smk.admin.controller;

import com.smk.admin.service.SysRoleService;
import com.smk.admin.vo.req.PageReq;
import com.smk.admin.vo.req.SysRoleReq;
import com.smk.common.annotation.BizLog;
import com.smk.common.model.SysRole;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.common.vo.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @Description: 角色管理
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/23 10:57
 * Copyright (c) , .
 */
@Controller
@RequestMapping("/role")
@Slf4j
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 跳转到角色管理
     *
     * @return
     */
    @GetMapping("/roleManage")
    public String toPage() {
        log.info("进入角色管理");
        return "/role/roleManage";
    }

    /**
     * 分页获取角色列表
     *
     * @param pageReq
     * @return
     */
    @PostMapping("/getRoleList")
    @ResponseBody
    @BizLog("获取角色列表")
    public PageResponse getRoleList(PageReq pageReq) {
        // 获取角色列表
        return sysRoleService.getRolesByPage(pageReq);
    }


    /**
     * 获取所有角色
     *
     * @return
     */
    @GetMapping("/getAllRoles")
    @ResponseBody
    @BizLog("获取角色列表")
    public List<SysRole> getAllRoles() {

        return sysRoleService.getRoles(new SysRole());
    }

    /**
     * 新增或更新角色
     *
     * @param role
     * @return
     */
    @PostMapping("/saveOrUpdateRole")
    @ResponseBody
    @BizLog("新增或更新角色")
    @RequiresPermissions("role:edit")
    public BaseResult saveOrUpdateRole(SysRoleReq role) {
        if (StringUtils.isEmpty(role.getRoleCode()) || StringUtils.isEmpty(role.getRoleName())) {
            return BaseResultBuilder.buildBadRequestResult("角色编码和角色名称均不可为空");
        }

        if (role.getId() == null) {
            //新增角色
            return sysRoleService.addRole(role);
        } else { //更新角色权限后需要该角色用户退出登录时才生效，这里先不清缓存
            //修改角色
            return sysRoleService.updateRole(role);
        }
    }


    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @PostMapping("/delRole")
    @ResponseBody
    @BizLog("删除角色")
    @RequiresPermissions("role:edit")
    public BaseResult delRole(int id) {
        return sysRoleService.delRole(id);

    }

}
