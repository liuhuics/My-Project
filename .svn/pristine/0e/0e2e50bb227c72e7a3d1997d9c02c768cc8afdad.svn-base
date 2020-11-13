package com.smk.common.dao;

import com.smk.common.config.BatchTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/12/2 16:40
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BatchInsertMapper {

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * batch方式插入
     *
     * @param list        需要插入的数据
     * @param batchTag 继承自BatchMapper的实现类
     * @param <T>
     */
    @Transactional
    public <T> void insertBatch(List<T> list, BatchTag batchTag) {
        log.info("batch insert start：" + list.size() + "条");

        // 初始化批量处理的sql session
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        for (int i = 0; i < list.size(); i++) {
            batchTag.insertSelective(list.get(i));
        }
        session.commit();
        session.clearCache();
        log.info("batch insert end！");
    }

}
