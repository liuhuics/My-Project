package com.smk.common.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * Table: SYS_ROLE_PERMIT
 */
@Setter
@Getter
public class SysRolePermit extends SysRolePermitKey implements Serializable {
    /**
     * 创建时间
     *
     * Column:    CREATE_TIME
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;
}