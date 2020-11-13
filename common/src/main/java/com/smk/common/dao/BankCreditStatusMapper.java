package com.smk.common.dao;

import com.smk.common.model.BankCreditStatus;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author linpeng
 */
public interface BankCreditStatusMapper {

    int deleteByPrimaryKey(String id);

    int insert(BankCreditStatus record);

    int insertSelective(BankCreditStatus record);

    BankCreditStatus selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BankCreditStatus record);

    int updateByPrimaryKey(BankCreditStatus record);

    int batchInsert(@Param("recordList") List<BankCreditStatus> recordList);

    /**
     * 删除该产品编号下所有数据
     * @param prodId
     * @return
     */
    int deleteByProId(String prodId);
}