package com.smk.business.netty.client.longConnection;

import lombok.*;

/**
 * @Description: host和ip封装类，一定要重写hashcode()和equals()
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/9/14 15:43
 * Copyright (c) 2020
 */
@Data
@NoArgsConstructor
public class ServerAdress {

    private String host;

    private int port;
}
