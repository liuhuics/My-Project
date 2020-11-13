package com.smk.common.dao;

import com.smk.common.model.SysPermission;
import com.smk.common.model.SysRole;
import com.smk.common.model.SysRolePermit;
import com.smk.common.model.SysRolePermitKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRolePermitMapper {
    int deleteByPrimaryKey(SysRolePermitKey key);

    int insert(SysRolePermit record);

    int insertSelective(SysRolePermit record);

    SysRolePermit selectByPrimaryKey(SysRolePermitKey key);

    int updateByPrimaryKeySelective(SysRolePermit record);

    int updateByPrimaryKey(SysRolePermit record);

    int batchInsert(@Param("recordList") List<SysRolePermit> recordList);

    /**
     * 获取某角色下的所有权限
     *
     * @param roleId
     * @return
     */
    List<SysPermission> getPermitsByRoleId(Integer roleId);

    /**
     * 删除某角色下分配的权限
     *
     * @param roleId
     */
    void deletePermitsByRoleId(Integer roleId);

    /**
     * 获取在使用某权限的角色
     *
     * @param permId
     * @return
     */
    List<SysRole> getRolesByPermId(Integer permId);

}