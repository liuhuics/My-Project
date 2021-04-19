package com.smk.common.netty.constant;

import org.apache.commons.compress.utils.CharsetNames;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/8/25 14:07
 * Copyright (c) 2020
 */
public class NettyConstant {

    public static final Charset PROTOCOL_ENCODING = Charset.forName(CharsetNames.UTF_8);

    public static final int MAGIC_NUMBER = 534288;

    public static final int VERSION = 1;

    public static final int BEAT_INTERVAL = 30;

    public static final int BEAT_TIMEOUT = 3 * BEAT_INTERVAL;

    public static final int DEFAULT_SERVER_PORT=9092;
}
