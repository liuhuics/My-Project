package com.smk.common.dao;

import com.smk.common.model.CommonMsgTemplate;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommonMsgTemplateMapper {
    int deleteByPrimaryKey(String msgId);

    int insert(CommonMsgTemplate record);

    int insertSelective(CommonMsgTemplate record);

    CommonMsgTemplate selectByPrimaryKey(String msgId);

    int updateByPrimaryKeySelective(CommonMsgTemplate record);

    int updateByPrimaryKeyWithBLOBs(CommonMsgTemplate record);

    int updateByPrimaryKey(CommonMsgTemplate record);

    int batchInsert(@Param("recordList") List<CommonMsgTemplate> recordList);
}