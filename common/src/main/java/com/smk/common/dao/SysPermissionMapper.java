package com.smk.common.dao;

import com.smk.common.model.SysPermission;
import com.smk.common.vo.admin.SysPermissionResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    int batchInsert(@Param("recordList") List<SysPermission> recordList);

    /**
     * 获取所有权限
     *
     * @return
     */
    List<SysPermission> getAllPermits();

    /**
     * 获取所有最后一级权限
     *
     * @return
     */
    List<SysPermission> getAllChildPermits();

    /**
     * 获取所有最后一级权限，并将父菜单列表名字一并查出
     *
     * @return
     */
    List<SysPermissionResp> getAllPermitsWithPName();

    /**
     * 获取某权限下的所有子权限
     *
     * @param pId
     * @return
     */
    List<SysPermission> getChildPermitsByPid(Integer pId);


    /**
     * 根据角色编码或名称查询角色列表
     *
     * @param permName 角色名称
     * @param permCode 角色编码
     * @return
     */
    List<SysPermission> getPermitsByCodeOrName(String permName, String permCode);

}