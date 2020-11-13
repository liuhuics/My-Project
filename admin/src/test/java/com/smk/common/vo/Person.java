package com.smk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/11 14:15
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private int age;
    private BigDecimal salary;
}
