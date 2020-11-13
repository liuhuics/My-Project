package com.smk.common.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Table: BANK_FILE_RECORD
 */
@Data
public class BankFileRecord implements Serializable {
    /**
     * 主键
     *
     * Column:    ID
     */
    private Long id;

    /**
     * 产品编号
     *
     * Column:    PRODUCT_ID
     */
    private String productId;

    /**
     * 本地文件名称
     *
     * Column:    LOCAL_FILE_NAME
     */
    private String localFileName;

    /**
     * 本地文件路径
     *
     * Column:    LOCAL_FILE_PATH
     */
    private String localFilePath;

    /**
     * 下载状态 0：未下载 1：已下载 2:下载失败
     *
     * Column:    DOWNLOAD_STS
     */
    private String downloadSts;

    /**
     * 处理状态 0：未处理 1：已处理 2：处理失败
     *
     * Column:    DEAL_STS
     */
    private String dealSts;

    /**
     * 文件日期
     *
     * Column:    FILE_DATE
     */
    private Date fileDate;

    /**
     * 失败原因
     *
     * Column:    FAIL_MSG
     */
    private String failMsg;

    /**
     * 处理时间
     *
     * Column:    DEAL_TIME
     */
    private Date dealTime;

    /**
     * 创建时间
     *
     * Column:    CREATE_TIME
     */
    private Date createTime;

    /**
     * 返回码
     *
     * Column:    RETURN_CODE
     */
    private String returnCode;

    /**
     * 返回信息
     *
     * Column:    RETURN_MSG
     */
    private String returnMsg;

    private static final long serialVersionUID = 1L;
}