package com.smk.business.netty.client.longConnection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/14 15:43
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerAdress {

    private String host;

    private int port;
}
