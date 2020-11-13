package com.smk.common.vo;

import lombok.*;

/**
 * @Description: 与mcp模块交互返回基类，仅在返回类型为void时返回该类。如果想打印返回信息，只需要覆写 toString()
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/8 16:38
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseResult {

    /**
     * 默认200
     */
    private Integer code = 200;

    private String msg;

}
