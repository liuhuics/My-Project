package com.smk.common.netty.message;

import com.smk.common.netty.constant.MsgType;
import com.smk.common.netty.constant.NettyConstant;
import com.smk.common.util.JsonUtil;
import com.smk.common.util.SerialNumberUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 13:40
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Getter
@Setter
public class RequestMsgPacket extends BaseMsgPacket {
    /**
     * 接口全类名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数签名对应的Class
     */
    private Class[] methodArgumentClasses;

    /**
     * 方法参数
     */
    private Object[] methodArguments;

    /**
     * 参数返回对应的Class
     */
    private Class<?> methodReturnType;


}
