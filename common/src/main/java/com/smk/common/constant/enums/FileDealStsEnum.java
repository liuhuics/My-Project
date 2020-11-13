package com.smk.common.constant.enums;

/**
 * 文件处理状态
 *
 * @author zhangwj
 * @date 2019-11-26
 */
public enum FileDealStsEnum {
    /**
     * 处理成功
     */
    SUCCESS("1", "处理成功"),
    /**
     * 处理中
     */
    PROCESSING("2", "处理中"),
    /**
     * 处理失败
     */
    FAILED("0", "处理失败");

    FileDealStsEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

