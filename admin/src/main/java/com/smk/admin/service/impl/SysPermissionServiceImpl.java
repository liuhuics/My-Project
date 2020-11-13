package com.smk.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.smk.admin.constant.SysConstant;
import com.smk.admin.service.SysPermissionService;
import com.smk.admin.vo.req.PageReq;
import com.smk.admin.vo.resp.TreeNodeVO;
import com.smk.common.dao.SysPermissionMapper;
import com.smk.common.dao.SysRoleMapper;
import com.smk.common.dao.SysRolePermitMapper;
import com.smk.common.model.SysPermission;
import com.smk.common.model.SysRole;
import com.smk.common.model.SysUser;
import com.smk.common.util.DateUtil;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.common.vo.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/7 14:43
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Service
@Slf4j
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermitMapper sysRolePermitMapper;


    @Override
    public List<SysPermission> getAllChildPermits() {
        return sysPermissionMapper.getAllChildPermits();
    }

    @Override
    public List<SysPermission> getAllChildPermitsforRole(Integer roleId) {
        return sysRolePermitMapper.getPermitsByRoleId(roleId);
    }

    @Override
    public SysPermission getById(int id) {
        return sysPermissionMapper.selectByPrimaryKey(id);
    }


    @Override
    public PageResponse getPermissionList(PageReq pageReq) {

        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());

        return new PageResponse(sysPermissionMapper.getAllPermitsWithPName());
    }

    @Override
    public List<SysPermission> getParentPermissionList() {

        return sysPermissionMapper.getChildPermitsByPid(SysConstant.ROOT_TREE_ID);
    }

    @Override
    public BaseResult addPermission(SysPermission sysPermission) {
        List<SysPermission> sysPermissions = sysPermissionMapper.getPermitsByCodeOrName(sysPermission.getPermName(),
                sysPermission.getPermCode());
        if (sysPermissions.size() > 0) {
            return BaseResultBuilder.buildBadRequestResult("存在同编码或同名的权限");
        }
        // 添加权限
        sysPermissionMapper.insertSelective(sysPermission);
        return BaseResultBuilder.buildSuccessResult();
    }

    @Override
    public BaseResult updatePermission(SysPermission sysPermission) {
        //更新权限
        List<SysPermission> sysPermissions = sysPermissionMapper.getPermitsByCodeOrName(sysPermission.getPermName(),
                sysPermission.getPermCode());
        if (sysPermissions.size() > 1 || (sysPermissions.size() == 1 && sysPermission.getId().intValue() != sysPermissions.get(0).getId())) {
            return BaseResultBuilder.buildBadRequestResult("存在同编码或同名的权限");
        }
        sysPermission.setUpdateTime(DateUtil.getCurrentDate());
        sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);
        return BaseResultBuilder.buildSuccessResult();
    }


    @Override
    public BaseResult delPermit(Integer id) {
        List<SysPermission> childPermitList = sysPermissionMapper.getChildPermitsByPid(id);
        //该权限是否尚有子权限
        if (childPermitList.size() > 0) {
            String childPermits =
                    childPermitList.stream().map((permit) -> permit.getPermName()).reduce((a, b) -> a + "," + b).get();
            return BaseResultBuilder.buildBadRequestResult("该权限不可删除，因其下尚有子权限：" + childPermits);
        }
        //该权限是否尚有角色使用
        List<SysRole> sysRoles = sysRolePermitMapper.getRolesByPermId(id);
        if (sysRoles.size() > 0) {
            String sysRoleNames =
                    sysRoles.stream().map((sysRole) -> sysRole.getRoleName()).reduce((a, b) -> a + "," + b).get();
            return BaseResultBuilder.buildBadRequestResult("该权限不可删除，因其尚有以下角色使用：" + sysRoleNames);
        }
        sysPermissionMapper.deleteByPrimaryKey(id);
        return BaseResultBuilder.buildSuccessResult();
    }


    @Override
    public List<TreeNodeVO> getUserPerms(SysUser user) {
        Integer roleId = user.getRoleId();

        List<SysPermission> permissions = sysRolePermitMapper.getPermitsByRoleId(roleId);

        List<TreeNodeVO> treeNodeVOS = buildPermissionTree(permissions);
//        List<TreeNodeVO> treeNodeVOS = buildPermissionTreePerfect(permissions);

        return treeNodeVOS;
    }

    /**
     * 获取权限树，仅支持两级权限
     *
     * @param sysPermissions
     * @return
     */
    private List<TreeNodeVO> buildPermissionTree(List<SysPermission> sysPermissions) {

        List<TreeNodeVO> treeNodeVOS = new ArrayList<>();

        //仅有两级菜单，而权限设置在第二级菜单上，如主菜单有系统管理，而其下有用户管理、角色管理，则权限给到用户管理这一层级上
        Map<Integer, TreeNodeVO> map = new HashMap<>();
        Set<String> permitUrls = new HashSet<>();
        for (SysPermission sysPermission : sysPermissions) {

            //同一个tab页会分至少两个权限，一个edit，一个view
            String permissionUrls = sysPermission.getUrl();
            //如果不小心把父权限也加进去了，就不处理
            if (StringUtils.isEmpty(permissionUrls)) {
                continue;
            }
            if (!permitUrls.contains(permissionUrls)) {
                permitUrls.add(permissionUrls);
            } else {
                continue;
            }

            int pid = sysPermission.getPid();
            //父权限只查一次
            if (map.get(pid) == null) {

                SysPermission parentSysPermission = sysPermissionMapper.selectByPrimaryKey(pid);
                if (parentSysPermission == null) {
                    continue;
                }
                TreeNodeVO parentNode = new TreeNodeVO();
                parentNode.setId(parentSysPermission.getId());
                parentNode.setName(parentSysPermission.getPermName());
                parentNode.setUrl(parentSysPermission.getUrl());

                map.put(pid, parentNode);
            }

            TreeNodeVO childNode = new TreeNodeVO();
            childNode.setId(sysPermission.getId());
            childNode.setName(sysPermission.getPermName());
            childNode.setUrl(permissionUrls);


            map.get(pid).getChildren().add(childNode);


        }

        treeNodeVOS.addAll(map.values());
        return treeNodeVOS;
    }


    /**
     * 获取权限树，支持多级权限，但前端需要同时支持才行，不然没用
     *
     * @param sysPermissions
     * @return
     */
    private List<TreeNodeVO> buildPermissionTreePerfect(List<SysPermission> sysPermissions) {
        Map<String, List<SysPermission>> result = getAllPermitsForUser(sysPermissions);
        List<SysPermission> topPermissions = result.get("top");
        List<SysPermission> validPermissions = result.get("valid");

        List<TreeNodeVO> treeNodeVOS = new ArrayList<>();

        for (SysPermission topPermission : topPermissions) {
            TreeNodeVO parentNode = new TreeNodeVO();
            treeNodeVOS.add(parentNode);

            parentNode.setId(topPermission.getId());
            parentNode.setName(topPermission.getPermName());
            parentNode.setUrl(topPermission.getUrl());
            buildOneTree(parentNode, validPermissions);
        }

        return treeNodeVOS;
    }

    /**
     * 获取某一个一级节点树
     *
     * @param topNode
     * @param permissions
     */
    private void buildOneTree(TreeNodeVO topNode, List<SysPermission> permissions) {
        List<TreeNodeVO> childern = new ArrayList<>();
        for (SysPermission sysPermission : permissions) {
            if (sysPermission.getPid().intValue() == topNode.getId()) {
                TreeNodeVO childNode = new TreeNodeVO();

                childNode.setId(sysPermission.getId());
                childNode.setName(sysPermission.getPermName());
                childNode.setUrl(sysPermission.getUrl());
                childern.add(childNode);
                buildOneTree(childNode, permissions);
            }
        }
        topNode.setChildren(childern);
    }


    /**
     * 获取用户权限
     *
     * @param sysPermissions
     * @return 会返回两部分，一部分是一级权限，另一个部分是用户所有权限，不限某级节点
     */
    private Map<String, List<SysPermission>> getAllPermitsForUser(List<SysPermission> sysPermissions) {

        List<SysPermission> validPermissions = new ArrayList<>();

        Map<String, List<SysPermission>> result = new HashMap<>();

        List<SysPermission> topPermission = new ArrayList<>();
        result.put("top", topPermission);
        result.put("valid", validPermissions);

        List<SysPermission> allPermissions = sysPermissionMapper.getAllPermits();
        Set<String> permitUrls = new HashSet<>();
        Set<Integer> permitIds = new HashSet<>();
        Set<Integer> parentIdSet = new HashSet<>();

        for (SysPermission sysPermission : sysPermissions) {

            //同一个tab页会分至少两个权限，一个edit，一个view，故不需要重复加
            if (!permitUrls.contains(sysPermission.getUrl())) {
                permitUrls.add(sysPermission.getUrl());
                validPermissions.add(sysPermission);

                //此部分是为了避免用户配置权限时仅配置最底层一级的权限，连其上权限也配置的情况
                permitIds.add(sysPermission.getId());
                if (sysPermission.getPid() == SysConstant.ROOT_TREE_ID) {
                    topPermission.add(sysPermission);
                    continue;
                }
            } else {
                continue;
            }

            for (SysPermission tempSysPermission : allPermissions) {

                int tempId = tempSysPermission.getId();
                //找到了sysPermission的父权限且父权限未添加过时
                if (tempId == sysPermission.getPid() && !permitIds.contains(tempId)) {
                    //如果该父权限未添加过，则添加
                    if (!parentIdSet.contains(tempId)) {
                        parentIdSet.add(tempId);
                        validPermissions.add(tempSysPermission);

                        sysPermission = tempSysPermission;
                        int pid = tempSysPermission.getPid();
                        if (pid == SysConstant.ROOT_TREE_ID) {
                            topPermission.add(tempSysPermission);
                            break;
                        }

                    }

                }
            }


        }
        return result;
    }


}
