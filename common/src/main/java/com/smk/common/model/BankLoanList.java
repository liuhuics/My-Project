package com.smk.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * Table: BANK_LOAN_LIST
 */
@Data
public class BankLoanList implements Serializable {
    /**
     * 主键
     *
     * Column:    ID
     */
    private String id;

    /**
     * 产品编号
     *
     * Column:    PROD_ID
     */
    private String prodId;

    /**
     * 身份证号
     *
     * Column:    ID_NUMBER
     */
    private String idNumber;

    /**
     * 对账日期
     *
     * Column:    RECONCILIATION_DATE
     */
    private String reconciliationDate;

    /**
     * 借据编号
     *
     * Column:    DEBIT_NO
     */
    private String debitNo;

    /**
     * 放款时间
     *
     * Column:    LOAD_DATE
     */
    private String loadDate;

    /**
     * 合同到期时间(该笔借据的结束时间)
     *
     * Column:    CONTRACT_EXPIRY_DATE
     */
    private String contractExpiryDate;

    /**
     * 借款总金额
     *
     * Column:    LOAN_TOTAL_AMT
     */
    private BigDecimal loanTotalAmt;

    /**
     * 还款日
     *
     * Column:    REPAY_DAY
     */
    private Short repayDay;

    /**
     * 分期期数
     *
     * Column:    PERIODS
     */
    private Short periods;

    /**
     * 借据状态
     *
     * Column:    DEBIT_STS
     */
    private String debitSts;

    /**
     * 在贷余额
     *
     * Column:    REMAIN_LOAN_AMT
     */
    private BigDecimal remainLoanAmt;

    /**
     * 创建时间
     *
     * Column:    CREATE_TIME
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}