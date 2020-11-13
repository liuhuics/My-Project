package com.smk.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/13 10:50
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Setter
@Getter
public class ExampleResp extends BaseResult {

    private int count;

    private List<Example> examples;

    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
}
