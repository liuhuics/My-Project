package com.smk.common.dao;

import com.smk.common.model.ProdInfo;

import java.util.List;

public interface ProdInfoMapper {

    int insert(ProdInfo record);

    int insertSelective(ProdInfo record);

    /**
     * 查询有效的产品信息
     *
     * @return ProdInfo
     */
    List<ProdInfo> selectValidProduct();

    /**
     * 查询产品信息
     *
     * @param prodId
     * @return ProdInfo
     */
    ProdInfo selectByPrimaryKey(String prodId);
}