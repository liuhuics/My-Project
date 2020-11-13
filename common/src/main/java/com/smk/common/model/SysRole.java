package com.smk.common.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * Table: SYS_ROLE
 */
@Setter
@Getter
public class SysRole implements Serializable {
    /**
     * 主键
     *
     * Column:    ID
     */
    private Integer id;

    /**
     * 角色代码，如 admin
     *
     * Column:    ROLE_CODE
     */
    private String roleCode;

    /**
     * 角色名称
     *
     * Column:    ROLE_NAME
     */
    private String roleName;

    /**
     * 是否是管理员，1表示是，0表示否
     *
     * Column:    IS_ADMIN
     */
    private String admin;

    /**
     * 备注
     *
     * Column:    REMARK
     */
    private String remark;

    /**
     * 创建时间
     *
     * Column:    CREATE_TIME
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * Column:    UPDATE_TIME
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}