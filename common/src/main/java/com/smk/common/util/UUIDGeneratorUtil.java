/**
 *
 */
package com.smk.common.util;

import java.util.UUID;

/**
 * @Description:UNID工具类
 * @Project: finance-parent
 * @author: linPeng
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 20200303 12:00:00
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
public class UUIDGeneratorUtil {

    /**
     * 生成UNID入口
     * @return
     */
    public static String genUNID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
