package com.smk.common.interceptor;

import com.smk.common.constant.CommonConstant;
import com.smk.common.exception.BizException;
import com.smk.common.exception.ParamInvalidException;
import com.smk.common.util.CommonUtil;
import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/8 16:37
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 拦截其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult handleException(Exception e) {

        log.error("controller层出现异常", e);
        return BaseResultBuilder
                .buildServerErrorResult(StringUtils.isEmpty(e.getMessage()) ? "后台模块处理时出现异常，详见异常堆栈" : e.getMessage());
    }

    /**
     * 拦截其他异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public BaseResult handleUnException(UnauthorizedException e) {
        String errorMsg = e.getMessage();
        return BaseResultBuilder.buildServerErrorResult("无此角色或权限" + errorMsg.substring(errorMsg.indexOf("[")) +
                "，请联系管理员");
    }

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public BaseResult handleBizException(BizException e) {
        return BaseResultBuilder.buildServerErrorResult(e.getMessage());
    }

    /**
     * 拦截参数不合法异常
     */
    @ExceptionHandler(ParamInvalidException.class)
    @ResponseBody
    public BaseResult handleParamNotValidException(ParamInvalidException e) {
        return BaseResultBuilder.buildBadRequestResult(e.getMessage());

    }

    /**
     * 拦截自动较验参数(@RequestBody)不合法异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseBody
    public BaseResult handleMethodArgumentNotValidException(Exception e) {
        log.error("参数较验未通过！", e);
        BindingResult result = null;
        if (e instanceof MethodArgumentNotValidException) {

            result = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            result = ((BindException) e).getBindingResult();

        }
        List<FieldError> fieldErrors = result.getFieldErrors();

        String errorMsg = CommonUtil.populateFieldErrors(fieldErrors);
        return BaseResultBuilder
                .buildBadRequestResult(errorMsg.length() > 0 ? errorMsg.substring(0, errorMsg.length() - 1) : "");

    }

    /**
     * 拦截自动较验参数(@RequestParam)不合法异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public BaseResult handleViolationException(ValidationException e) {
        log.error("参数较验未通过！", e);
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;

            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> item : violations) {
                sb.append(item.getMessage() + CommonConstant.LOG_SPLIT);
            }

            return BaseResultBuilder.buildBadRequestResult(sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "");
        }
        return BaseResultBuilder.buildBadRequestResult(e.getMessage());
    }


}
