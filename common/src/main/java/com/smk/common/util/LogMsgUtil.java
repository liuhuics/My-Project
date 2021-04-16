package com.smk.common.util;

import com.smk.common.annotation.BizLog;
import com.smk.common.constant.CommonConstant;
import com.smk.common.constant.CustomLogType;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

/**
 * @Description:组装日志内容
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/8 11:01
 * Copyright (c) , .
 */
public class LogMsgUtil {

    //sl4j log format
    public static String sl4jLogFormat(String methodName, String actionFlag, String customMessge) {
        StringBuilder logStr = new StringBuilder();
        logStr.append(methodName).append(CommonConstant.LOG_SPLIT).append(actionFlag).append(CommonConstant.LOG_SPLIT)
                .append(customMessge);
        return logStr.toString();
    }

    public static String timeLogFormat(String msg, Long consumeTime) {
        StringBuilder logStr = new StringBuilder();
        logStr.append(msg).append(CommonConstant.LOG_SPLIT).append(consumeTime);
        return logStr.toString();
    }

    public static String bizLogFormat(String... customMessge) {
        StringBuilder logStr = new StringBuilder();
        for (String s : customMessge) {
            logStr.append(s).append(CommonConstant.LOG_SPLIT);
        }
        return logStr.substring(0, logStr.length() - 1);
    }

    /**
     * 只打印请求参数
     *
     * @param request
     * @return
     */
    public static String logRequest(HttpServletRequest request) {
        StringBuilder logStr = new StringBuilder();
        logStr.append("请求url：").append(request.getRequestURL() + " ").append(CommonConstant.LOG_SPLIT).append("请求参数:")
                .append(convertParameterMap2String(request.getParameterMap()));
        return logStr.toString();

        //append("请求参数:").append(request.getQueryString())，该参数仅对应requestParam，即url？后的部分
    }

    /**
     * 将请求参数封装
     *
     * @param parameterMap
     * @return
     */
    private static String convertParameterMap2String(Map<String, String[]> parameterMap) {
        if (parameterMap.size() == 0) {
            return "";
        }
        StringBuilder logStr = new StringBuilder();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            logStr.append(entry.getKey() + "=" + Arrays.toString(entry.getValue()) + ",");

        }
        return logStr.substring(0, logStr.length() - 1);
    }

    /**
     * 组装切点和请求体中的内容
     *
     * @param traceId
     * @param request
     * @param joinPoint
     * @return
     */
    public static String logRequest(String traceId, HttpServletRequest request, JoinPoint joinPoint) {
        StringBuilder logStr = new StringBuilder();
        logStr.append(CommonConstant.TRACE_ID).append("：").append(traceId).append(CommonConstant.LOG_SPLIT)
                .append("类-方法 : ").append(getSimpleClassAndMethod(joinPoint)).append(CommonConstant.LOG_SPLIT)
                .append("请求url：").append(request.getRequestURL() + " ").append(CommonConstant.LOG_SPLIT).append("请求参数：")
                .append(convertParameterMap2String(request.getParameterMap())).append(CommonConstant.LOG_SPLIT)
                .append("请求体：").append(getRequestBody(joinPoint));
        return logStr.toString();
    }

    /**
     * 获取类名和方法名
     *
     * @param joinPoint
     * @return
     */
    public static String getClassAndMethod(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        return className + "." + joinPoint.getSignature().getName();

    }

    /**
     * 获取简短类名和方法名
     *
     * @param joinPoint
     * @return
     */
    public static String getSimpleClassAndMethod(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        return className.substring(className.lastIndexOf(".") + 1) + "." + joinPoint.getSignature().getName();

    }

    /**
     * 打印业务日志内容
     *
     * @param joinPoint
     * @param logSuffix          日志内容后缀，如开始、结束、出错等
     * @param bizDescThreadLocal 记录业务描述的线程本地变量
     */
    public static void logBizInfo(JoinPoint joinPoint, String logSuffix, NamedThreadLocal<String> bizDescThreadLocal) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        BizLog bizLog = methodSignature.getMethod().getAnnotation(BizLog.class);

        if (bizLog == null) {
            return;
        }
        String description = bizLog.value();
        if (StringUtils.isBlank(description)) {
            return;
        }
        bizDescThreadLocal.set(description);

        CustomLogType.BIZ_INFO.info(description + CommonConstant.LOG_SPLIT + logSuffix);
    }

    /**
     * 获取方法中@RequestBody注解的内容
     *
     * @param joinPoint
     * @return
     */
    private static String getRequestBody(JoinPoint joinPoint) {
        String str = "";
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();
        int index = -1;
        for (Annotation[] annotations : annotationMatrix) {
            index++;
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof RequestBody))
                    continue;
                Object requestBody = joinPoint.getArgs()[index];
                return JsonUtil.object2Json(requestBody);
            }
        }
        return str;
    }

}
