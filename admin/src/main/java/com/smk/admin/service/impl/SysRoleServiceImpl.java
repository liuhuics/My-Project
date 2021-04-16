package com.smk.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.smk.admin.constant.SysConstant;
import com.smk.admin.service.SysRoleService;
import com.smk.admin.vo.req.PageReq;
import com.smk.admin.vo.req.SysRoleReq;
import com.smk.common.constant.CommonConstant;
import com.smk.common.dao.SysPermissionMapper;
import com.smk.common.dao.SysRoleMapper;
import com.smk.common.dao.SysRolePermitMapper;
import com.smk.common.model.SysPermission;
import com.smk.common.model.SysRole;
import com.smk.common.model.SysRolePermit;
import com.smk.common.model.SysUser;
import com.smk.common.util.DateUtil;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.common.vo.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/7 14:42
 * Copyright (c) 2020
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {


    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermitMapper sysRolePermitMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    public SysRole getById(Integer id) {
        return sysRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Set<String> getPermissionsByRoleId(Integer roleId) {
        //连同父类的权限一并给了
        List<SysPermission> permissions = sysRolePermitMapper.getPermitsByRoleId(roleId);
        Set<String> permitCodes = new HashSet<>();
        for (SysPermission sysPermission : permissions) {
            permitCodes.add(sysPermission.getPermCode());
            int pid = sysPermission.getPid();
            while (SysConstant.ROOT_TREE_ID != pid) {
                SysPermission temp = sysPermissionMapper.selectByPrimaryKey(pid);
                pid = temp.getPid();
                permitCodes.add(temp.getPermCode());
            }
        }
        return permitCodes;
    }


    @Override
    public PageResponse getRolesByPage(PageReq pageReq) {

        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
        List<SysRole> roles = sysRoleMapper.getRoleList(new SysRole());
        //TODO 获取权限


        return new PageResponse(roles);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult addRole(SysRoleReq roleReq) {
        String roleName = roleReq.getRoleName();
        String roleCode = roleReq.getRoleCode();

        List<SysRole> sysRoles = sysRoleMapper.getRolesByRoleCodeOrRoleName(roleName, roleCode);
        if (sysRoles.size() > 0) {
            return BaseResultBuilder.buildBadRequestResult("该角色编码或角色名称已被使用");
        }

        SysRole sysRole = new SysRole();
        sysRole.setRoleCode(roleReq.getRoleCode());
        sysRole.setRoleName(roleReq.getRoleName());
        sysRole.setRemark(roleReq.getRemark());
        //插入角色
        sysRoleMapper.insertSelective(sysRole);

        //把id拿出来
        int id = sysRole.getId();

        //如果权限非空，则插入权限
        return handleRolePermissions(id, roleReq.getPermissions(), false);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult updateRole(SysRoleReq roleReq) {
        int id = roleReq.getId();
        String roleName = roleReq.getRoleName();
        String roleCode = roleReq.getRoleCode();

        List<SysRole> sysRoles = sysRoleMapper.getRolesByRoleCodeOrRoleName(roleName, roleCode);
        if (sysRoles.size() > 1 || (sysRoles.size() == 1 && sysRoles.get(0).getId().intValue() != id)) {
            return BaseResultBuilder.buildBadRequestResult("该角色编码或角色名称已被使用");
        }

        //其余情况直接更新
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleReq, sysRole);
        sysRole.setUpdateTime(DateUtil.getCurrentDate());
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);

        //接着更新权限，其实是先删再添
        return handleRolePermissions(id, roleReq.getPermissions(), true);

    }


    @Override
    public BaseResult delRole(Integer id) {
        List<SysUser> sysUsers = sysRoleMapper.getSysUsersByRoleId(id);
        if (sysUsers.size() > 0) {
            String userName =
                    sysUsers.stream().map((sysUser) -> sysUser.getUserName()).reduce((a, b) -> a + "," + b).
                            get();
            return BaseResultBuilder.buildBadRequestResult("该角色下尚有用户，用户为：" + userName);
        } else {
            sysRoleMapper.deleteByPrimaryKey(id);
            //同时删除角色分配的所有权限
            sysRolePermitMapper.deletePermitsByRoleId(id);
            return BaseResultBuilder.buildSuccessResult();
        }

    }


    @Override
    public List<SysRole> getRoles(SysRole sysRole) {
        return sysRoleMapper.getRoleList(sysRole);
    }


    /**
     * 处理角色权限，如果新增角色，则直接插入权限，否则先删再插
     *
     * @param roleId      角色id
     * @param permissions 权限列表，以逗号拼接
     * @param isUpdate    是否更新操作
     * @return
     */
    private BaseResult handleRolePermissions(int roleId, String permissions, boolean isUpdate) {
        if (StringUtils.isEmpty(permissions)) {
            return BaseResultBuilder.buildSuccessResult();
        }
        List<SysRolePermit> sysRolePermits = new ArrayList<>();
        String[] permissionArray = permissions.split(",");
        for (String permission : permissionArray) {
            SysRolePermit sysRolePermit = new SysRolePermit();
            sysRolePermits.add(sysRolePermit);
            sysRolePermit.setRoleId(roleId);
            sysRolePermit.setCreateTime(DateUtil.getCurrentDate());
            sysRolePermit.setPermId(Integer.parseInt(permission));
        }

        if (sysRolePermits.size() == 0) {
            return BaseResultBuilder.buildSuccessResult();
        }

        //如果是更新操作，则先删除旧的权限
        if (isUpdate) {
            sysRolePermitMapper.deletePermitsByRoleId(roleId);
        }

        //然后插入新的权限
        int sqlNum = 0;
        while (sqlNum + CommonConstant.ONCE_BATCH_INSERT_NUM < sysRolePermits.size()) {
            List<SysRolePermit> subList = sysRolePermits.subList(sqlNum,
                    sqlNum += CommonConstant.ONCE_BATCH_INSERT_NUM);
            sysRolePermitMapper.batchInsert(subList);
        }

        //最后50条
        List<SysRolePermit> subList = sysRolePermits.subList(sqlNum, sysRolePermits.size());
        sysRolePermitMapper.batchInsert(subList);
        return BaseResultBuilder.buildSuccessResult();
    }

}
