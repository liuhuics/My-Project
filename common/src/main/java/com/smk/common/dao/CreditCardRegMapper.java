package com.smk.common.dao;

import com.smk.common.model.CreditCardReg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CreditCardRegMapper {

    int insert(CreditCardReg record);

    int insertSelective(CreditCardReg record);

    int batchInsert(@Param("recordList") List<CreditCardReg> recordList);

    /**
     * 用产品编号和创建日期查询当天数据
     *
     * @param creditCardReg
     * @return
     */
    List<CreditCardReg> selectByProdIdAndSysDate(CreditCardReg creditCardReg);

    /**
     * 产品编号、创建日期查询人数
     *
     * @param creditCardReg
     * @return
     */
    int selectByProdIdAndSysDateCount(CreditCardReg creditCardReg);

}