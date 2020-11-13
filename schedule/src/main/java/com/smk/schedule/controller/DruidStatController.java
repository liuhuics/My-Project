package com.smk.schedule.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Project: smkLoan
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/12 16:30
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@RestController
public class DruidStatController {

    @GetMapping("/druid/stat")
    public Object druidStat(){
        // DruidStatManagerFacade#getDataSourceStatDataList 该方法可以获取所有数据源的监控数据，除此之外 DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}
