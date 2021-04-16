package com.smk.common.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/11 18:33
 * Copyright (c) 2019
 */
@Setter
@Getter
public class ExampleBody {

    @Min(1)
    private int page;

    @Min(1)
    private int pageSize;

    //自定义日期转换格式
    //    @JsonFormat(pattern = "yyyyMMdd",timezone = "GMT+8")
    private Date date;

}
