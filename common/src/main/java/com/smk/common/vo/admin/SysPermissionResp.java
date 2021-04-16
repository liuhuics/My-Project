package com.smk.common.vo.admin;

import com.smk.common.model.SysPermission;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/9 15:23
 * Copyright (c) 2020
 */
@Setter
@Getter
public class SysPermissionResp extends SysPermission {

    /**
     * 父权限名称，该属性如果用pName，则转成json后就变成了pname
     */
    private String parentName;
}
