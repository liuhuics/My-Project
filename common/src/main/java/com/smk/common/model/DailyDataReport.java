package com.smk.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Table: DAILY_DATA_REPORT
 */
@Setter
@Getter
public class DailyDataReport implements Serializable {
    /**
     * 主键
     * <p>
     * Column:    ID
     */
    private String id;

    /**
     * 创建时间
     * <p>
     * Column:    CREATE_TIME
     */
    private Date createTime;

    /**
     * 点击人数
     * <p>
     * Column:    CLICK_NUM
     */
    private BigDecimal clickNum;

    /**
     * 申请人数
     * <p>
     * Column:    APPLY_NUM
     */
    private BigDecimal applyNum;

    /**
     * 完成申请信息填写人数
     * <p>
     * Column:    INFORMATION_NUM
     */
    private BigDecimal informationNum;

    /**
     * 完成填写率(%) 如实际上为5.8%，存储为5.8
     * <p>
     * Column:    INFORMATION_PERCENTAGE
     */
    private BigDecimal informationPercentage;

    /**
     * 授信人数
     * <p>
     * Column:    CREDIT_NUM
     */
    private BigDecimal creditNum;

    /**
     * 授信金额
     * <p>
     * Column:    CREDIT_SUM
     */
    private BigDecimal creditSum;

    /**
     * 平均授信金额
     * <p>
     * Column:    CREDIT_BALANCE_AVG
     */
    private BigDecimal creditBalanceAvg;

    /**
     * 授信通过率 如实际上为5.8%，存储为5.8
     * <p>
     * Column:    CREDIT_PERCENTAGE
     */
    private BigDecimal creditPercentage;

    /**
     * 借款人数
     * <p>
     * Column:    BORROWERS_NUM
     */
    private BigDecimal borrowersNum;

    /**
     * 再借人数
     * <p>
     * Column:    BORROWING_NUM
     */
    private BigDecimal borrowingNum;

    /**
     * 线上人均借款金额
     * <p>
     * Column:    LOAN_AVG
     */
    private BigDecimal loanAvg;

    /**
     * 借款笔数
     * <p>
     * Column:    LOAN_COUNT
     */
    private BigDecimal loanCount;

    /**
     * 贷款规模
     * <p>
     * Column:    LOAN_VOLUME
     */
    private BigDecimal loanVolume;

    /**
     * 贷款余额
     * <p>
     * Column:    LOAN_BALANCE
     */
    private BigDecimal loanBalance;

    /**
     * 还款笔数
     * <p>
     * Column:    REPAYMENT_COUNT
     */
    private BigDecimal repaymentCount;

    /**
     * 还款金额
     * <p>
     * Column:    REPAYMENT_SUM
     */
    private BigDecimal repaymentSum;

    /**
     * 逾期金额
     * <p>
     * Column:    OVERDUE_SUM
     */
    private BigDecimal overdueSum;

    /**
     * 逾期笔数
     * <p>
     * Column:    OVERDUE_COUNT
     */
    private BigDecimal overdueCount;

    /**
     * 已收利息
     * <p>
     * Column:    INTEREST
     */
    private BigDecimal interest;

    /**
     * 已收罚息
     * <p>
     * Column:    PENALTY_INTEREST
     */
    private BigDecimal penaltyInterest;

    /**
     * 平台实收利息
     * <p>
     * Column:    SYS_INTEREST
     */
    private BigDecimal sysInterest;

    /**
     * 平台实收罚息
     * <p>
     * Column:    SYS_PENALTY_INTEREST
     */
    private BigDecimal sysPenaltyInterest;

    /**
     * 统计日期
     * <p>
     * Column:    STATIS_DATE
     */
    private Date statisDate;

    /**
     * 产品编号
     * <p>
     * Column:    PRODUCT_ID
     */
    private String productId;

    private static final long serialVersionUID = 1L;
}