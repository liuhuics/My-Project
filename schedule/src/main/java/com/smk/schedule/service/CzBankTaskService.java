package com.smk.schedule.service;

import com.smk.common.model.BankFileRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 稠州银行定时任务service
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/3/23 16:16
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
public interface CzBankTaskService {

    /**
     * 稠州银行文件下载执行入口
     *
     * @param execDate
     * @return
     * @throws Exception
     */
    List<BankFileRecord> czBankFileDown(Date execDate) throws Exception;

    /**
     * 处理稠州授信文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    void dealCzBankCreditStatus(BankFileRecord bankFileRecord) throws Exception;

    /**
     * 处理稠州日报文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    void dealCzBankDailyReport(BankFileRecord bankFileRecord) throws Exception;

    /**
     * 处理稠州借据文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    void dealCzBankLoanList(BankFileRecord bankFileRecord) throws Exception;

    /**
     * 处理无需处理的文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    void dealUnnecessaryiles(BankFileRecord bankFileRecord) throws Exception;

    /**
     * 稠州银行任务执行失败发送短信
     *
     * @param param
     * @throws Exception
     */
    void sendFaildMsg(Map<String, Object> param) throws Exception;
}
