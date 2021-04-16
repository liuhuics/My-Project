package com.smk.common.annotation;

import java.lang.annotation.*;

/**
 * @Description: 用于设置Excel文件中的列名
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/13 16:46
 * Copyright (c) 2019
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Target({ ElementType.FIELD })
public @interface ExcelColumnName {

    /**
     * 导出的Excel文件的列名
     *
     * @return
     */
    String value() ;
}
