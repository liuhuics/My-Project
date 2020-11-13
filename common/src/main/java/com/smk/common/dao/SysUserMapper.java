package com.smk.common.dao;

import com.smk.common.model.SysUser;
import com.smk.common.vo.admin.SysUserResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int batchInsert(@Param("recordList") List<SysUser> recordList);

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    SysUser getByUserName(String userName);

    /**
     * 根据用户名查询有效的用户
     *
     * @param userName
     * @return
     */
    SysUser getValidUserByUserName(String userName);

    /**
     * 根据一定的查询条件查询所有用户， 如果sysUser中各属性为空，则查询所有用户
     *
     * @param sysUser
     * @return
     */
    List<SysUserResp> getUsers(SysUser sysUser);

    /**
     * 根据用户的名字更新用户的密码
     *
     * @param userName
     * @param userPwd
     */
    void updatePwdByUserName(@Param("userName") String userName, @Param("userPwd") String userPwd);
}