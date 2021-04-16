package com.smk.schedule.service;

import com.smk.common.dao.BankFileRecordMapper;
import com.smk.common.dao.CommonMsgTemplateMapper;
import com.smk.common.dao.CommonSystemParamMapper;
import com.smk.common.exception.BizException;
import com.smk.common.model.BankFileRecord;
import com.smk.common.model.CommonMsgTemplate;
import com.smk.common.model.CommonSystemParam;
import com.smk.common.service.SFTPService;
import com.smk.common.util.DateUtil;
import com.smk.schedule.constant.ScheduleTaskConstant;
import com.smk.schedule.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Description:银行文件处理基类
 * @Project: finance-parent
 * @author: linPeng
 * @version: 1.0
 * @since: JDK 1.7
 * @Date: 20200324 17:00:00
 * Copyright (c) 2020
 */
@Slf4j
public abstract class AbstractBankFileService extends SFTPService {

    protected String sendMsgCode;

    protected String sendMsgId;

    @Autowired
    private BankFileRecordMapper bankFileRecordMapper;

    @Autowired
    private CommonSystemParamMapper commonSystemParamMapper;

    @Autowired
    private CommonMsgTemplateMapper commonMsgTemplateMapper;


    /**
     * 获取待处理银行文件名称
     *
     * @param fileDate
     * @return
     */
    protected abstract List<String> getFileNames(Date fileDate);

    /**
     * 获取本地存储路径
     *
     * @param execDate
     * @return
     */
    protected abstract String getLocalFilePath(Date execDate);

    /**
     * 下载银行文件
     *
     * @param bankFileRecord
     * @param execDate
     * @param fileDate
     */
    protected abstract void download(BankFileRecord bankFileRecord, Date fileDate, Date execDate);

    /**
     * 银行文件登记
     *
     * @param fileDate
     * @param execDate
     * @param productId
     */
    protected void checkFileRegister(Date fileDate, Date execDate, String productId) {
        //获取昨日文件集合
        List<String> fileNames = getFileNames(fileDate);

        //登记查看文件是否下载成功并已处理
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fileDate", fileDate);
        paramMap.put("productId", productId);
        List<BankFileRecord> bankFileRecords = bankFileRecordMapper.selectInfoByProductIdAndFileDate(paramMap);
        if (bankFileRecords == null || bankFileRecords.size() == 0) {
            //初始化文件登记承载变量
            bankFileRecords = new ArrayList<>();

            //新建存储目录
            File file = new File(getLocalFilePath(execDate));
            if (!file.exists()) {
                file.mkdirs();
            }

            //文件登记
            for (String fileName : fileNames) {
                BankFileRecord bankFileRecord = new BankFileRecord();
                bankFileRecord.setProductId(productId);
                bankFileRecord.setLocalFileName(fileName);
                bankFileRecord.setLocalFilePath(getLocalFilePath(execDate) + fileName);
                bankFileRecord.setDownloadSts(ScheduleTaskConstant.BANK_FILE_UN_DOWNLOAD);
                bankFileRecord.setDealSts(ScheduleTaskConstant.BANK_FILE_UN_DEAL);
                bankFileRecord.setCreateTime(DateUtil.getCurrentTimestamp());
                bankFileRecord.setFileDate(fileDate);
                bankFileRecords.add(bankFileRecord);
            }
            bankFileRecordMapper.batchInsert(bankFileRecords);
        }
    }

    /**
     * 银行文件下载
     *
     * @param fileDate
     * @param execDate
     * @param productId
     * @return
     * @throws Exception
     */
    protected List<BankFileRecord> downloadBankFile(Date fileDate, Date execDate, String productId) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("fileDate", fileDate);
        paramMap.put("productId", productId);
        List<BankFileRecord> bankFileRecords = bankFileRecordMapper.selectInfoByProductIdAndFileDate(paramMap);
        if (bankFileRecords == null || bankFileRecords.size() == 0) {
            log.error("下载失败，原因：文件登记失败，productId、fileDate为" + productId + DateUtil.getDate8(fileDate));
            throw new BizException("下载失败，原因：文件登记失败，productId、fileDate为" + productId + DateUtil.getDate8(fileDate));
        }

        //判断是否存在未下载的文件 未下载则下载
        for (BankFileRecord bankFileRecord : bankFileRecords) {
            String downloadSts = bankFileRecord.getDownloadSts();
            if (!StringUtils.equals(downloadSts, ScheduleTaskConstant.BANK_FILE_DOWNLOAD_SUCC)) {
                try {
                    log.info("银行文件下载 文件名{} 文件地址{}", bankFileRecord.getLocalFileName(), bankFileRecord.getLocalFilePath());
                    download(bankFileRecord, fileDate, execDate);
                    bankFileRecordMapper.updateByPrimaryKeySelective(bankFileRecord);
                } catch (Exception e) {
                    log.error("银行文件下载错误 文件名{} 错误原因{}", bankFileRecord.getLocalFileName(), e);
                    bankFileRecord.setDownloadSts(ScheduleTaskConstant.BANK_FILE_DOWNLOAD_FAIL);
                    bankFileRecord.setFailMsg(e.getMessage());
                    bankFileRecordMapper.updateByPrimaryKeySelective(bankFileRecord);
                    throw new BizException("银行文件下载错误 文件名:" + bankFileRecord.getLocalFileName() + " 错误原因:" + e);
                }
            }
        }

        //判断是否全部下载完成
        for (BankFileRecord bankFileRecord : bankFileRecords) {
            String downloadSts = bankFileRecord.getDownloadSts();
            if (!StringUtils.equals(downloadSts, ScheduleTaskConstant.BANK_FILE_DOWNLOAD_SUCC)) {
                log.error("存在没有下载成功的银行文件文件，解析终止，fileDate:" + DateUtil.getDate8(fileDate));
                throw new BizException("存在没有下载成功的银行文件文件，解析终止，fileDate:" + DateUtil.getDate8(fileDate));
            }
        }
        return bankFileRecords;
    }

    /**
     * 解析文件通用方法
     *
     * @param bankFileRecord
     * @return
     * @throws Exception
     */
    protected List<String> analysisFile(BankFileRecord bankFileRecord) throws Exception {
        File file = new File(bankFileRecord.getLocalFilePath());
        if (!file.exists()) {
            log.error("文件不存在:" + bankFileRecord.getLocalFileName());
            throw new Exception("文件不存在:" + bankFileRecord.getLocalFileName());
        }
        List<String> dataList;
        try {
            dataList = FileUtils.readLines(file, "UTF-8");
        } catch (IOException e) {
            log.error(bankFileRecord.getLocalFileName() + ",文件数据读取错误:" + e);
            throw new Exception(bankFileRecord.getLocalFileName() + ",文件数据读取错误:" + e);
        }
        if (dataList.size() == 0) {
            log.error(bankFileRecord.getLocalFileName() + ",文件数据为空！");

            //数据文件为空发送短信
            Map<String, Object> param = new HashMap<>();
            param.put("productId", bankFileRecord.getProductId());
            param.put("fileName", bankFileRecord.getLocalFileName() + "文件为空");
            try {
                bankTaskFailSendMsg(param, sendMsgCode, "BANK_FILE_EMPTY");
            } catch (Exception ex) {
                log.error("短信发送失败，时间：" + DateUtil.getCurrentTimestamp(), ex);
            }
        }
        return dataList;
    }


    /**
     * 银行文件任务执行失败发送短信
     * 指定发送发送短信配置，如不指定则使用默认配置
     *
     * @param param
     * @param sendMsgCode
     * @param sendMsgId
     * @throws Exception
     */
    protected void bankTaskFailSendMsg(Map<String, Object> param, String sendMsgCode, String sendMsgId) throws Exception {
        if (StringUtils.isBlank(sendMsgCode)) {
            sendMsgCode = this.sendMsgCode;
        }
        if (StringUtils.isBlank(sendMsgId)) {
            sendMsgId = this.sendMsgId;
        }
        CommonSystemParam commonSystemParam = commonSystemParamMapper.selectByPrimaryKey(sendMsgCode);
        CommonMsgTemplate commonMsgTemplate = commonMsgTemplateMapper.selectByPrimaryKey(sendMsgId);
        SendMsgUtil.assembledMsg(param, commonMsgTemplate);
        String mobileStr = commonSystemParam.getParamValue();
        if (StringUtils.isNotBlank(mobileStr)) {
            String[] mobiles = StringUtils.split(mobileStr, "|");
            for (String mobile : mobiles) {
                SendMsgUtil.sendMsg(mobile, commonMsgTemplate.getMsgContent());
            }
        }
    }

    /**
     * 更新文件登记表信息
     *
     * @param bankFileRecord
     */
    protected void updateBankFileReordSts(BankFileRecord bankFileRecord) {
        bankFileRecordMapper.updateByPrimaryKeySelective(bankFileRecord);
    }
}
