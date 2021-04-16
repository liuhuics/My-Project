package com.smk.common.vo;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageSerializable;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/9 14:50
 * Copyright (c) 2020
 */
@Setter
@Getter
public class PageResponse<T> {
    private int code;
    private String msg;

    private long total;

    private List<T> list;

    public PageResponse(List<T> list) {
        this.list = list;
        if(list instanceof Page){
            this.total = ((Page)list).getTotal();
        } else {
            this.total = list.size();
        }

        this.code = 200;
        this.msg = "";

    }
}
