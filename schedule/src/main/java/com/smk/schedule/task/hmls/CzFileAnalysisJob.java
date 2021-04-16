package com.smk.schedule.task.hmls;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.smk.common.model.BankFileRecord;
import com.smk.common.util.DateUtil;
import com.smk.schedule.annotation.ElasticSimpleJob;
import com.smk.schedule.constant.ScheduleTaskConstant;
import com.smk.schedule.service.CzBankTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:小贷稠州银行文件下载解析处理
 * @Project: finance-parent
 * @author: linPeng
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/28 10:39
 * Copyright (c) 2019
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ElasticSimpleJob("0 0/5 9-12 * * ?")
public class CzFileAnalysisJob implements SimpleJob {

    private final CzBankTaskService czBankTaskService;

    @Override
    public void execute(ShardingContext shardingContext) {
        try {
            Date exeDate;
            String jobParamter = shardingContext.getJobParameter();
            if (StringUtils.isBlank(jobParamter)) {
                exeDate = DateUtil.getDate();
            } else {
                exeDate = DateUtils.parseDate(jobParamter, DateUtil.FULL_SIMPLE_DAY_FORMAT);
            }
            log.info("稠州银行文件下载解析开始 exeDate：{}", DateUtil.getDate8(exeDate));

            //文件下载
            List<BankFileRecord> bankFileRecords = czBankTaskService.czBankFileDown(exeDate);

            //文件解析
            for (BankFileRecord bankFileRecord : bankFileRecords) {
                String dealSts = bankFileRecord.getDealSts();
                if (StringUtils.equals(dealSts, ScheduleTaskConstant.BANK_FILE_DEAL_SUCC)) {
                    log.info("文件已成功处理 无需重新处理 文件名称：" + bankFileRecord.getLocalFileName());
                    continue;
                }
                String fileDateStr = DateUtil.getDate8(bankFileRecord.getFileDate());
                String fileName = bankFileRecord.getLocalFileName();
                String[] splitStrArray = fileName.split(fileDateStr);
                switch (splitStrArray[0]) {
                    case ScheduleTaskConstant.CZ_APPLY_STATUS: {
                        //稠州银行授信文件 不支持重跑，授信记录应为最新记录
                        czBankTaskService.dealCzBankCreditStatus(bankFileRecord);
                        break;
                    }
                    case ScheduleTaskConstant.CZ_DAILY_REPORT: {
                        //稠州银行日报表 支持重跑
                        czBankTaskService.dealCzBankDailyReport(bankFileRecord);
                        break;
                    }
                    case ScheduleTaskConstant.CZ_LOAN_LIST: {
                        //稠州银行借据文件 不支持重跑，借据记录应为最新记录
                        czBankTaskService.dealCzBankLoanList(bankFileRecord);
                        break;
                    }
                    default:
                        czBankTaskService.dealUnnecessaryiles(bankFileRecord);
                        break;
                }
            }
            log.info("稠州银行文件下载解析成功 exeDate：{}", DateUtil.getDate8(exeDate));
        } catch (Exception e) {
            log.error("稠州银行文件下载解析处理执行失败：", e);

            //发送短信
            Map<String, Object> param = new HashMap<>();
            param.put("taskName", "稠州银行文件处理任务");
            param.put("reason", "稠州银行文件下载解析处理执行失败：" + e.getMessage());
            try {
                czBankTaskService.sendFaildMsg(param);
            } catch (Exception ex) {
                log.error("短信发送失败，时间：" + DateUtil.getCurrentTimestamp(), ex);
            }
        }
    }
}
