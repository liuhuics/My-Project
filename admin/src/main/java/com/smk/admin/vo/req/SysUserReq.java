package com.smk.admin.vo.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/9 15:00
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Setter
@Getter
public class SysUserReq extends PageReq {
    private String sysUserName;
    private String userPhone;
}
