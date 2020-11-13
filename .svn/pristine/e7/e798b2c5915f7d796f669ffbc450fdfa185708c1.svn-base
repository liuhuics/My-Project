package com.smk.plugin.batchInsert;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/19 17:52
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */

import org.mybatis.generator.api.IntrospectedTable;

/**
 * <b><code>BaseGenTool</code></b>
 * <p/>
 * Description:
 * <p/>
 * <b>Creation Time:</b> 2018/12/16 16:23.
 *
 * @author HuWeihui
 */
public class BaseGenTool {

    /**
     * 判断是不是Mybatis3运行生成的.
     *
     * @param introspectedTable the introspected table
     * @return the boolean
     * @author HuWeihui
     * @since hui_project v1
     */
    public static boolean isMybatisMode(IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime().equals(IntrospectedTable.TargetRuntime.MYBATIS3)) {
            return true;
        }
        return false;
    }

}

