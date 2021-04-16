package com.smk.admin.vo.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/23 11:03
 * Copyright (c) 2020
 */
@Setter
@Getter
public class PageReq {
    private Integer pageNum;
    private Integer pageSize;
}
