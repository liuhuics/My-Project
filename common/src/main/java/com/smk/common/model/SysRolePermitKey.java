package com.smk.common.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysRolePermitKey implements Serializable {
    /**
     * 角色id
     *
     * Column:    ROLE_ID
     */
    private Integer roleId;

    /**
     * 权限ID
     *
     * Column:    PERM_ID
     */
    private Integer permId;

    private static final long serialVersionUID = 1L;
}