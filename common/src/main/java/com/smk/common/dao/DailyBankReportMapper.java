package com.smk.common.dao;

import com.smk.common.model.DailyBankReport;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author linpeng
 */
public interface DailyBankReportMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DailyBankReport record);

    int insertSelective(DailyBankReport record);

    DailyBankReport selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DailyBankReport record);

    int updateByPrimaryKey(DailyBankReport record);

    int batchInsert(@Param("recordList") List<DailyBankReport> recordList);

    /**
     * 指定产品编号和报表日期删除数据
     *
     * @param record
     * @return
     */
    int deleteByProdIdAndReportDate(DailyBankReport record);
}