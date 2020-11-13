package com.smk.common.dao;

import com.smk.common.model.SysRole;
import com.smk.common.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    int batchInsert(@Param("recordList") List<SysRole> recordList);

    /**
     * 根据一定条件查询角色列表，sysRole里属性为空时则查询全部角色
     *
     * @param sysRole
     * @return
     */
    List<SysRole> getRoleList(SysRole sysRole);

    /**
     * 获取某角色下的所有用户
     *
     * @param roleId
     * @return
     */
    List<SysUser> getSysUsersByRoleId(Integer roleId);

    /**
     * 查看是否有roleCode或roleName为指定参数的记录,roleName非空，roleCode可为null，这样即是仅查roleName为某值的记录
     *
     * @param roleName
     * @param roleCode
     * @return
     */
    List<SysRole> getRolesByRoleCodeOrRoleName(String roleName, String roleCode);
}