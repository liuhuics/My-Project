package com.smk.common.dao;

import com.smk.common.model.CommonSystemParam;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommonSystemParamMapper {
    int deleteByPrimaryKey(String paramCode);

    int insert(CommonSystemParam record);

    int insertSelective(CommonSystemParam record);

    CommonSystemParam selectByPrimaryKey(String paramCode);

    int updateByPrimaryKeySelective(CommonSystemParam record);

    int updateByPrimaryKey(CommonSystemParam record);

    int batchInsert(@Param("recordList") List<CommonSystemParam> recordList);
}