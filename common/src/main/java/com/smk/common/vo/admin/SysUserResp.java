package com.smk.common.vo.admin;

import com.smk.common.model.SysUser;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/9 15:23
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Setter
@Getter
public class SysUserResp extends SysUser {

    private String roleName;
}
