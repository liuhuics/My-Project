package com.smk.common.model;

import java.io.Serializable;
import lombok.Data;

/**
 * Table: COMMON_SYSTEM_PARAM
 */
@Data
public class CommonSystemParam implements Serializable {
    /**
     * 参数代码
     *
     * Column:    PARAM_CODE
     */
    private String paramCode;

    /**
     * 参数类型
     *
     * Column:    PARAM_TYPE
     */
    private String paramType;

    /**
     * 参数名称
     *
     * Column:    PARAM_NAME
     */
    private String paramName;

    /**
     * 参数值
     *
     * Column:    PARAM_VALUE
     */
    private String paramValue;

    /**
     * 参数序号
     *
     * Column:    PARAM_SEQ
     */
    private String paramSeq;

    /**
     * 参数描述
     *
     * Column:    PARAM_DESC
     */
    private String paramDesc;

    private static final long serialVersionUID = 1L;
}