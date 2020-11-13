package com.smk.common.dao;

import com.smk.common.model.BankLoanList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author linpeng
 */
public interface BankLoanListMapper {

    int deleteByPrimaryKey(String id);

    int insert(BankLoanList record);

    int insertSelective(BankLoanList record);

    BankLoanList selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BankLoanList record);

    int updateByPrimaryKey(BankLoanList record);

    int batchInsert(@Param("recordList") List<BankLoanList> recordList);

    /**
     * 删除该产品编号下所有数据
     * @param prodId
     * @return
     */
    int deleteByProId(String prodId);
}