package com.smk.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * Table: DAILY_BANK_REPORT
 * @author linpeng
 */
@Data
public class DailyBankReport implements Serializable {
    /**
     * 主键id
     *
     * Column:    ID
     */
    private Long id;

    /**
     * 产品id
     *
     * Column:    PROD_ID
     */
    private String prodId;

    /**
     * 日报时间
     *
     * Column:    REPORT_DATE
     */
    private String reportDate;

    /**
     * 完成申请信息填写人次
     *
     * Column:    APPLY_NUM
     */
    private Integer applyNum;

    /**
     * 当完成申请信息填写人次
     *
     * Column:    DAY_APPLY_NUM
     */
    private Integer dayApplyNum;

    /**
     * 完成申请信息填写人数
     *
     * Column:    DIST_APPLY_NUM
     */
    private Integer distApplyNum;

    /**
     * 当完成申请信息填写人数
     *
     * Column:    DIST_DAY_APPLY_NUM
     */
    private Integer distDayApplyNum;

    /**
     * 授信人数
     *
     * Column:    CREDIT_NUM
     */
    private Integer creditNum;

    /**
     * 当日授信人数
     *
     * Column:    DAY_CREDIT_NUM
     */
    private Integer dayCreditNum;

    /**
     * 授信总金额
     *
     * Column:    CREDIT_AMT
     */
    private BigDecimal creditAmt;

    /**
     * 当日授信总金额
     *
     * Column:    DAY_CREDIT_AMT
     */
    private BigDecimal dayCreditAmt;

    /**
     * 授信通过率%=授信人数/完成申请信息填写人数，保留两位小数
     *
     * Column:    CREDIT_PASS_RATE
     */
    private BigDecimal creditPassRate;

    /**
     * 借款人数
     *
     * Column:    LOAN_NUM
     */
    private Integer loanNum;

    /**
     * 当日借款人数
     *
     * Column:    DAY_LOAN_NUM
     */
    private Integer dayLoanNum;

    /**
     * 借款笔数
     *
     * Column:    LOAN_COUNT
     */
    private Long loanCount;

    /**
     * 借款金额
     *
     * Column:    LOAN_AMT
     */
    private BigDecimal loanAmt;

    /**
     * 最新贷款总余额
     *
     * Column:    LOAN_BANLANCE
     */
    private BigDecimal loanBanlance;

    /**
     * 还款笔数
     *
     * Column:    REPAY_COUNT
     */
    private Long repayCount;

    /**
     * 还款金额
     *
     * Column:    REPAY_AMT
     */
    private BigDecimal repayAmt;

    /**
     * 逾期笔数
     *
     * Column:    DELAY_COUNT
     */
    private Long delayCount;

    /**
     * 逾期金额
     *
     * Column:    DELAY_AMT
     */
    private BigDecimal delayAmt;

    /**
     * 已收利息
     *
     * Column:    TOTAL_INTEREST
     */
    private BigDecimal totalInterest;

    /**
     * 已收罚息
     *
     * Column:    TOTAL_PENALTY
     */
    private BigDecimal totalPenalty;

    /**
     * 每日新增授信通过客户的平均利率
     *
     * Column:    AVG_DAY_INTEREST_RATE
     */
    private BigDecimal avgDayInterestRate;

    /**
     * 创建时间
     *
     * Column:    CREATE_TIME
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}