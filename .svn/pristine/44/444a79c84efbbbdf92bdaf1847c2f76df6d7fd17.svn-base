package com.smk.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * Table: BANK_CREDIT_STATUS
 */
@Data
public class BankCreditStatus implements Serializable {
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
     * 授信状态 0 已拒绝，1 已通过
     *
     * Column:    CREDIT_STS
     */
    private String creditSts;

    /**
     * 授信金额
     *
     * Column:    CREDIT_AMT
     */
    private BigDecimal creditAmt;

    /**
     * 授信时间
     *
     * Column:    CREDIT_DATE
     */
    private String creditDate;

    /**
     * 授信利率（单位为年利率%） 例如：利率为2%，则存放2
     *
     * Column:    CREDIT_RATE
     */
    private BigDecimal creditRate;

    /**
     * 创建时间
     *
     * Column:    CREATE_TIME
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}