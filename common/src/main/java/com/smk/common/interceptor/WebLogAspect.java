package com.smk.common.interceptor;

import com.smk.common.constant.BizLogSuffix;
import com.smk.common.constant.CommonConstant;
import com.smk.common.constant.CustomLogType;
import com.smk.common.util.JsonUtil;
import com.smk.common.util.LogMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:拦截controller中请求和响应
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/8 11:59
 * Copyright (c) 2019
 */
//@Aspect
//@Component
@Slf4j
public class WebLogAspect {

    private NamedThreadLocal<Long> logTimeThreadLocal = new NamedThreadLocal<>("logTime");

    //traceId，便于不同模块间的日志追踪
    private NamedThreadLocal<String> traceIdThreadLocal = new NamedThreadLocal<>("traceId");

    //记录业务描述
    private NamedThreadLocal<String> bizDescThreadLocal = new NamedThreadLocal<>("bizDesc");

    @Pointcut("execution(public * com.smk.*.*controller.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            long beginTime = System.currentTimeMillis();//1、开始时间
            logTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）

            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            String traceId = request.getHeader(CommonConstant.TRACE_ID);
            traceIdThreadLocal.set(traceId);//线程绑定变量（该数据只有当前请求的线程可见）

            CustomLogType.HTTP_INFO.info(LogMsgUtil.logRequest(traceId, request, joinPoint));
            LogMsgUtil.logBizInfo(joinPoint, BizLogSuffix.START.getDescription(), bizDescThreadLocal);

        } catch (Exception e) {
            log.error("前置拦截controller方法时出错", e);
        }

    }

    @AfterReturning(returning = "result",
            pointcut = "webLog()")
    public void doAfterReturning(Object result) {

        try {

            String traceId = traceIdThreadLocal.get();

            long endTime = System.currentTimeMillis();//2、结束时间
            long beginTime = logTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
            long consumeTime = endTime - beginTime;//3、消耗的时间

            //请求-响应时间大于3000ms的记入long_time日志文件
            if (consumeTime > 3000) {
                CustomLogType.LONG_TIME.error(LogMsgUtil
                        .bizLogFormat(CommonConstant.TRACE_ID + ": " + traceId, "耗时较久：" + consumeTime / 1000 + "s"));
            }
            // 处理完请求，记录返回内容和返回时间
            CustomLogType.HTTP_INFO.info(LogMsgUtil
                    .bizLogFormat(CommonConstant.TRACE_ID + ": " + traceId, "返回:" + result,
                            "耗时：" + consumeTime + "ms"));

            String bizLogInfo = bizDescThreadLocal.get();
            if (StringUtils.isNotEmpty(bizLogInfo)) {

                CustomLogType.BIZ_INFO.info(bizLogInfo + CommonConstant.LOG_SPLIT + BizLogSuffix.END.getDescription());
            }
        } catch (Exception e) {
            log.error("拦截返回值时出现异常", e);

        } finally {
            //资源清理
            logTimeThreadLocal.remove();
            traceIdThreadLocal.remove();
            bizDescThreadLocal.remove();
        }
    }

    @AfterThrowing(pointcut = "webLog()",
            throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {

        try {
            String bizLogInfo = bizDescThreadLocal.get();
            if (StringUtils.isNotEmpty(bizLogInfo)) {
                CustomLogType.BIZ_INFO
                        .error(bizLogInfo + CommonConstant.LOG_SPLIT + BizLogSuffix.ERROR.getDescription());
            }

            CustomLogType.HTTP_INFO.error(LogMsgUtil.bizLogFormat(LogMsgUtil.getClassAndMethod(joinPoint)), ex);
        } catch (Exception e) {
            log.error("拦截异常返回时出现异常", e);

        } finally {
            logTimeThreadLocal.remove();
            traceIdThreadLocal.remove();
            bizDescThreadLocal.remove();

        }
    }

}
