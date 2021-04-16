package com.smk.admin.vo.resp;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 树节点
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/10 11:16
 * Copyright (c) , .
 */
@Setter
@Getter
public class TreeNodeVO implements Serializable {
    private int id;
    private String name;
    private String url;
    private List<TreeNodeVO> children = new ArrayList<>();
}
