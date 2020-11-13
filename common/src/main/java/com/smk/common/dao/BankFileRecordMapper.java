package com.smk.common.dao;

import com.smk.common.model.BankFileRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BankFileRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BankFileRecord record);

    int insertSelective(BankFileRecord record);

    BankFileRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BankFileRecord record);

    int updateByPrimaryKey(BankFileRecord record);

    int batchInsert(@Param("recordList") List<BankFileRecord> recordList);

    /**
     * fileName的名称数组、fileDate查询
     *
     * @param paramMap
     * @return
     */
    List<BankFileRecord> selectInfoByNameAndFileDate(Map<String, Object> paramMap);

    /**
     * productId、fileDate查询
     *
     * @param paramMap
     * @return
     */
    List<BankFileRecord> selectInfoByProductIdAndFileDate(Map<String, Object> paramMap);

    /**
     * 通过fileDate赋失败原因 用于解析标示为false时使用
     *
     * @param paramMap
     * @return
     */
    int updateInfoByFileDate(Map<String, Object> paramMap);

    /**
     * 通过文件名查找登记信息
     *
     * @param fileName
     * @return
     */
    BankFileRecord selectByFileName(String fileName);
}