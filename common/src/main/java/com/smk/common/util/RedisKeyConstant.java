package com.smk.common.util;

/**
 * @Description:redis相关key常量类 参考阿里云命名规范：https://yq.aliyun.com/articles/531067
 * (1)【建议】: 可读性和可管理性
 * 以业务名(或数据库名)为前缀(防止key冲突)，用冒号分隔，比如业务名:表名:id
 * ugc:video:1
 * <p>
 * (2)【建议】：简洁性
 * 保证语义的前提下，控制key的长度，当key较多时，内存占用也不容忽视，例如：
 * user:{uid}:friends:messages:{mid}简化为u:{uid}:fr:m:{mid}。
 * <p>
 * (3)【强制】：不要包含特殊字符
 * 反例：包含空格、换行、单双引号以及其他转义字符
 * @Project: smkLoan
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/15 14:00
 * Copyright (c) 2019
 */
public class RedisKeyConstant {

    /**
     * 订单号相关前缀
     */
    public static final String ORDER_NO_PREFIX = "orderNo:";

    /**
     * 管理台redis key 前缀
     */
    private static final String ADMIN_MODULE_PREFIX = "admin:";
    /**
     * 交易平台 redis key 前缀
     */
    private static final String BIZ_MODULE_PREFIX = "biz:";
    /**
     * 管理台角色
     */
    public static final String ROLE_PREFIX = ADMIN_MODULE_PREFIX + "role:";
    /**
     * 管理台角色的权限
     */
    public static final String ROLE_PERMIT_PREFIX = ADMIN_MODULE_PREFIX + "role:permit:";

}
