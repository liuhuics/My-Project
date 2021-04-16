package com.smk.common.config;

/**
 * @Description: 标识，用于executeBatch这种方式插入时使用。即如果要使用BatchInsertMapper的insertBatch方法，需要将自己的Mapper类继承BatchTag类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/2 16:57
 * Copyright (c) 2019
 */
public interface BatchTag<T> {

    int insertSelective(T t);
}
