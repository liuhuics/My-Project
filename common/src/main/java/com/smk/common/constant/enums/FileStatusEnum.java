package com.smk.common.constant.enums;

/**
 * 活动文件生效状态枚举类
 * @date 2019-11-26
 * @author zhangwj
 */
public enum FileStatusEnum {
    INVALID("0","已作废"),
    VALID("1","生效中");

    private String code;
    private String message;

    FileStatusEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

}
