package com.smk.schedule.service.impl;

import com.smk.common.constant.ProductConstant;
import com.smk.common.dao.BankCreditStatusMapper;
import com.smk.common.dao.BankLoanListMapper;
import com.smk.common.dao.DailyBankReportMapper;
import com.smk.common.exception.BizException;
import com.smk.common.model.BankCreditStatus;
import com.smk.common.model.BankFileRecord;
import com.smk.common.model.BankLoanList;
import com.smk.common.model.DailyBankReport;
import com.smk.common.util.DateUtil;
import com.smk.common.util.SplitUtil;
import com.smk.common.util.UUIDGeneratorUtil;
import com.smk.schedule.constant.ScheduleTaskConstant;
import com.smk.schedule.service.AbstractBankFileService;
import com.smk.schedule.service.CzBankTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 稠州银行接口基类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/3/23 16:16
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CzBankTaskServiceImpl extends AbstractBankFileService implements CzBankTaskService {

    @Value("${czBank.localFilePath}")
    private String localFilePath;

    @Value("${czBank.sftp.filePath}")
    private String sftpFilePath;

    private final BankCreditStatusMapper bankCreditStatusMapper;

    private final DailyBankReportMapper dailyBankReportMapper;

    private final BankLoanListMapper bankLoanListMapper;

    @Value("${czBank.sftp.host:localhost}")
    public void setHost(String host) {
        sftpHost = host;
    }

    @Value("${czBank.sftp.port:22}")
    public void setFtpPort(int port) {
        sftpPort = port;
    }

    @Value("${czBank.sftp.username:admin}")
    public void setFtpUsername(String username) {
        sftpUsername = username;
    }

    @Value("${czBank.sftp.password:admin}")
    public void setFtpPassword(String password) {
        sftpPassword = password;
    }

    @Value("${czBank.sftp.defaultUploadDir:/usr/test}")
    public void setDefaultDir(String defaultDir) {
        sftpDefaultUploadDir = defaultDir;
    }

    @Value("${czBank.sftp.privateKeyPath:}")
    public void setPrivateKeyPath(String keyPath) {
        privateKeyPath = keyPath;
    }

    @Value("${czBank.sftp.privateKeyPwd:}")
    public void setPrivateKeyPwd(String keyPwd) {
        privateKeyPwd = keyPwd;
    }

    @Value("${czBank.sendMsgCode}")
    public void setSendMsgCode(String sendMsgCode) {
        this.sendMsgCode = sendMsgCode;
    }

    @Value("${czBank.sendMsgId}")
    public void setSendMsgId(String sendMsgId) {
        this.sendMsgId = sendMsgId;
    }

    /**
     * 稠州银行文件下载执行入口
     *
     * @param execDate
     * @return
     * @throws Exception
     */
    @Override
    public List<BankFileRecord> czBankFileDown(Date execDate) throws Exception {
        try {
            //文件下载登记校验 获取文件时间
            Date fileDate = DateUtils.addDays(execDate, -1);

            //银行文件登记
            checkFileRegister(fileDate, execDate, ProductConstant.CZ_PRODUCT_ID);

            //银行文件下载
            return downloadBankFile(fileDate, execDate, ProductConstant.CZ_PRODUCT_ID);
        } catch (Exception e) {
            log.error("下载稠州银行时出错！", e);
            throw new BizException(e.getMessage());
        }
    }


    /**
     * 获取待处理银行文件名称
     *
     * @param fileDate
     * @return
     */
    @Override
    protected List<String> getFileNames(Date fileDate) {
        String fileDateStr = DateUtil.getDate8(fileDate);
        String flgFile = "DW_SMK_" + fileDateStr + ".FLG";
        String creditStatusFile = "DW_SMK_APPLY_STATUS_" + fileDateStr + ".TXT";
        String reportDayFile = "DW_SMK_DAILYREPORT_" + fileDateStr + ".TXT";
        String loanListFile = "DW_SMK_LOANLIST_" + fileDateStr + ".TXT";

        //待下载文件
        List<String> returnList = new ArrayList<>();
        returnList.add(flgFile);
        returnList.add(creditStatusFile);
        returnList.add(reportDayFile);
        returnList.add(loanListFile);
        return returnList;
    }


    /**
     * 获取本地存储路径
     *
     * @param execDate
     * @return
     */
    @Override
    protected String getLocalFilePath(Date execDate) {
        return localFilePath + DateUtil.getDate8(execDate) + File.separator;
    }

    /**
     * 下载银行文件
     *
     * @param bankFileRecord
     * @param fileDate
     * @param execDate
     */
    @Override
    protected void download(BankFileRecord bankFileRecord, Date fileDate, Date execDate) {
        String fileDateStr = DateUtil.getDate8(fileDate);
        String downFilePath = localFilePath + DateUtil.getDate8(execDate);
        String path = sftpFilePath + fileDateStr + File.separator;
        String flgFilePath = path + "DW_SMK_" + fileDateStr + ".FLG";
        boolean existFlg = isFileExists(flgFilePath);
        if (existFlg) {
            String aimFilePath = path + bankFileRecord.getLocalFileName();
            log.info("下载目标文件为：{}", aimFilePath);
            boolean flg = downloadFile(aimFilePath, downFilePath);
            if (flg) {
                bankFileRecord.setDownloadSts(ScheduleTaskConstant.BANK_FILE_DOWNLOAD_SUCC);
            } else {
                bankFileRecord.setDownloadSts(ScheduleTaskConstant.BANK_FILE_DOWNLOAD_FAIL);
            }
        } else {
            bankFileRecord.setDownloadSts(ScheduleTaskConstant.BANK_FILE_DOWNLOAD_FAIL);
            bankFileRecord.setFailMsg("FLG文件不存在，文件下载取消");
        }
    }

    /**
     * 处理稠州授信文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealCzBankCreditStatus(BankFileRecord bankFileRecord) throws Exception {
        //删除历史数据 每日全量更新
        bankCreditStatusMapper.deleteByProId(bankFileRecord.getProductId());
        List<BankCreditStatus> dataList = new ArrayList<>();

        //授信文件解析
        List<String> bankCreditStatusList = analysisFile(bankFileRecord);
        for (int i = 0; i < bankCreditStatusList.size(); i++) {
            log.info("正在解析稠州授信文件第" + i + "行");
            String line = bankCreditStatusList.get(i);
            String[] strArray = line.split("\\|");
            if (strArray.length != 5) {
                log.error("处理稠州授信文件失败，文件字段个数不为5");
                throw new Exception("处理稠州授信文件失败，文件字段个数不为5");
            }
            BankCreditStatus bankCreditStatus = new BankCreditStatus();
            bankCreditStatus.setIdNumber(strArray[0]);
            if (StringUtils.equals(strArray[1], "已拒绝")) {
                bankCreditStatus.setCreditSts(ScheduleTaskConstant.CZ_CREDIT_REFUSE);
            } else {
                bankCreditStatus.setCreditSts(ScheduleTaskConstant.CZ_CREDIT_THROUGH);
            }
            bankCreditStatus.setCreditAmt(SplitUtil.cleanDecimal(strArray[2]));
            bankCreditStatus.setCreditDate(SplitUtil.formatDate10toDate8(strArray[3]));
            bankCreditStatus.setCreditRate(SplitUtil.cleanDecimal(strArray[4]));
            bankCreditStatus.setProdId(bankFileRecord.getProductId());
            bankCreditStatus.setId(UUIDGeneratorUtil.genUNID());
            bankCreditStatus.setCreateTime(DateUtil.getCurrentTimestamp());
            dataList.add(bankCreditStatus);
        }
        if (dataList.size() != 0) {
            bankCreditStatusMapper.batchInsert(dataList);
        }
        bankFileRecord.setDealSts(ScheduleTaskConstant.BANK_FILE_DEAL_SUCC);
        bankFileRecord.setDealTime(DateUtil.getCurrentTimestamp());
        updateBankFileReordSts(bankFileRecord);
        log.info("处理稠州授信文件成功");
    }

    /**
     * 处理稠州日报文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealCzBankDailyReport(BankFileRecord bankFileRecord) throws Exception {
        bankFileRecord.setDealSts(ScheduleTaskConstant.BANK_FILE_DEAL_SUCC);
        bankFileRecord.setDealTime(DateUtil.getCurrentTimestamp());

        //支持重跑
        DailyBankReport dailyBankReport = new DailyBankReport();
        dailyBankReport.setProdId(bankFileRecord.getProductId());
        dailyBankReport.setReportDate(DateUtil.getDate8(bankFileRecord.getFileDate()));
        dailyBankReportMapper.deleteByProdIdAndReportDate(dailyBankReport);

        //稠州日报表文件解析
        List<String> dailyBankReportList = analysisFile(bankFileRecord);
        if (dailyBankReportList.size() == 0) {
            log.info("处理稠州日报表文件为空，文件无需处理");
            dailyBankReport.setCreateTime(DateUtil.getCurrentTimestamp());
            dailyBankReportMapper.insertSelective(dailyBankReport);
            updateBankFileReordSts(bankFileRecord);
            return;
        }

        if (dailyBankReportList.size() != 1) {
            log.error("处理稠州日报表文件失败，文件内容不为一行");
            throw new Exception("处理稠州日报表文件失败，文件内容不为一行");
        }
        String line = dailyBankReportList.get(0);
        String[] data = line.split("\\|");
        if (data.length != 21) {
            log.error("处理稠州日报表文件失败，文件字段个数不为21");
            throw new Exception("处理稠州日报表文件失败，文件字段个数不为21");
        }
        //完成申请信息填写人次
        dailyBankReport.setApplyNum(SplitUtil.cleanInteger(data[0]));
        //当日完成申请信息填写人次
        dailyBankReport.setDayApplyNum(SplitUtil.cleanInteger(data[1]));
        //完成申请信息填写人数
        dailyBankReport.setDistApplyNum(SplitUtil.cleanInteger(data[2]));
        //当日完成申请信息填写人数
        dailyBankReport.setDistDayApplyNum(SplitUtil.cleanInteger(data[3]));
        //授信人数
        dailyBankReport.setCreditNum(SplitUtil.cleanInteger(data[4]));
        //当日授信人数
        dailyBankReport.setDayCreditNum(SplitUtil.cleanInteger(data[5]));
        //授信总金额
        dailyBankReport.setCreditAmt(SplitUtil.cleanDecimal(data[6]));
        //当日授信总金额
        dailyBankReport.setDayCreditAmt(SplitUtil.cleanDecimal(data[7]));
        //授信通过率%=授信人数/完成申请信息填写人数，保留两位小数
        dailyBankReport.setCreditPassRate(SplitUtil.cleanDecimal(data[8]));
        //借款人数
        dailyBankReport.setLoanNum(SplitUtil.cleanInteger(data[9]));
        //当日借款人数
        dailyBankReport.setDayLoanNum(SplitUtil.cleanInteger(data[10]));
        //借款笔数
        dailyBankReport.setLoanCount(SplitUtil.formatLong(data[11]));
        //借款金额
        dailyBankReport.setLoanAmt(SplitUtil.cleanDecimal(data[12]));
        //最新贷款总余额
        dailyBankReport.setLoanBanlance(SplitUtil.cleanDecimal(data[13]));
        //还款笔数
        dailyBankReport.setRepayCount(SplitUtil.formatLong(data[14]));
        //还款金额
        dailyBankReport.setRepayAmt(SplitUtil.cleanDecimal(data[15]));
        //逾期笔数
        dailyBankReport.setDelayCount(SplitUtil.formatLong(data[16]));
        //逾期金额
        dailyBankReport.setDelayAmt(SplitUtil.cleanDecimal(data[17]));
        //已收利息
        dailyBankReport.setTotalInterest(SplitUtil.cleanDecimal(data[18]));
        //已收罚息
        dailyBankReport.setTotalPenalty(SplitUtil.cleanDecimal(data[19]));
        //每日新增授信通过客户的平均利率
        dailyBankReport.setAvgDayInterestRate(SplitUtil.cleanDecimal(data[20]));
        //创建时间
        dailyBankReport.setCreateTime(DateUtil.getCurrentTimestamp());
        dailyBankReportMapper.insertSelective(dailyBankReport);
        log.info("处理稠州日报表文件成功");
        updateBankFileReordSts(bankFileRecord);
    }

    /**
     * 处理稠州借据文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealCzBankLoanList(BankFileRecord bankFileRecord) throws Exception {
        //删除历史数据 每日全量更新
        bankLoanListMapper.deleteByProId(bankFileRecord.getProductId());
        List<BankLoanList> dataList = new ArrayList<>();

        //授信文件解析
        List<String> bankLoanLists = analysisFile(bankFileRecord);
        for (int i = 0; i < bankLoanLists.size(); i++) {
            log.info("正在解析稠州借据文件第" + i + "行");
            String line = bankLoanLists.get(i);
            String[] strArray = line.split("\\|");
            if (strArray.length != 10) {
                log.error("处理稠州借据文件失败，文件字段个数不为10");
                throw new Exception("处理稠州借据文件失败，文件字段个数不为10");
            }
            BankLoanList bankLoanList = new BankLoanList();
            bankLoanList.setId(UUIDGeneratorUtil.genUNID());
            bankLoanList.setProdId(bankFileRecord.getProductId());
            //身份证号
            bankLoanList.setIdNumber(strArray[0]);
            //对账日期
            bankLoanList.setReconciliationDate(strArray[1]);
            //借据编号
            bankLoanList.setDebitNo(strArray[2]);
            //放款时间
            bankLoanList.setLoadDate(SplitUtil.formatDate10toDate8(strArray[3]));
            //合同到期时间(该笔借据的结束时间)
            bankLoanList.setContractExpiryDate(SplitUtil.formatDate10toDate8(strArray[4]));
            //借款总金额
            bankLoanList.setLoanTotalAmt(SplitUtil.cleanDecimal(strArray[5]));
            //还款日
            bankLoanList.setRepayDay(SplitUtil.formatShort(strArray[6]));
            //分期期数
            bankLoanList.setPeriods(SplitUtil.formatShort(strArray[7]));
            //借据状态
            if (StringUtils.equals("使用中", strArray[8])) {
                bankLoanList.setDebitSts(ScheduleTaskConstant.DEBIT_IN_USE);
            } else {
                bankLoanList.setDebitSts(ScheduleTaskConstant.DEBIT_END);
            }
            //在贷余额
            bankLoanList.setRemainLoanAmt(SplitUtil.cleanDecimal(strArray[9]));
            bankLoanList.setCreateTime(DateUtil.getCurrentTimestamp());
            dataList.add(bankLoanList);
        }
        if (dataList.size() != 0) {
            bankLoanListMapper.batchInsert(dataList);
        }
        log.info("处理稠州借据文件成功");
        bankFileRecord.setDealSts(ScheduleTaskConstant.BANK_FILE_DEAL_SUCC);
        bankFileRecord.setDealTime(DateUtil.getCurrentTimestamp());
        updateBankFileReordSts(bankFileRecord);
    }

    /**
     * 处理无需处理的文件
     *
     * @param bankFileRecord
     * @throws Exception
     */
    @Override
    public void dealUnnecessaryiles(BankFileRecord bankFileRecord) throws Exception {
        bankFileRecord.setDealSts(ScheduleTaskConstant.BANK_FILE_DEAL_SUCC);
        bankFileRecord.setFailMsg("该文件无需处理");
        bankFileRecord.setDealTime(DateUtil.getCurrentTimestamp());
        updateBankFileReordSts(bankFileRecord);
    }

    /**
     * 稠州银行任务执行失败发送短信
     *
     * @param param
     * @throws Exception
     */
    @Override
    public void sendFaildMsg(Map<String, Object> param) throws Exception {
        bankTaskFailSendMsg(param, null, null);
    }
}
