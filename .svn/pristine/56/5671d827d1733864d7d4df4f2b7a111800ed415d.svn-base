package com.smk.common.vo;

import com.smk.common.constant.enums.ErrorInfoEnum;
import org.springframework.http.HttpStatus;

/**
 * @Description: 组装常用结果
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/26 10:25
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
public class BaseResultBuilder {

    /**
     * 返回成功的结果
     *
     * @return
     */
    public static BaseResult buildSuccessResult() {
        return new BaseResult(HttpStatus.OK.value(), "成功");
    }

    /**
     * 返回服务器错误的结果
     *
     * @param failMsg 失败详情
     * @return
     */
    public static BaseResult buildServerErrorResult(String failMsg) {
        return new BaseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), failMsg);
    }

    /**
     * 返回服务器错误的结果
     *
     * @param errorInfoEnum 失败详情
     * @return
     */
    public static BaseResult buildServerErrorResult(ErrorInfoEnum errorInfoEnum) {
        return new BaseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorInfoEnum.getMsg());
    }

    /**
     * 返回不合法请求的结果
     *
     * @param failMsg
     * @return
     */
    public static BaseResult buildBadRequestResult(String failMsg) {
        return new BaseResult(HttpStatus.BAD_REQUEST.value(), failMsg);
    }
}
