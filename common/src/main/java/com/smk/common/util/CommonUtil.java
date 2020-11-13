package com.smk.common.util;

import com.smk.common.constant.CommonConstant;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @Description:常用工具类
 * @Project: smkLoan
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/10/15 16:43
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
public class CommonUtil {

    /**
     * 有重复时以新值为准
     * @param hashMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> sortMapByKey(Map<K, V> hashMap) {
        Map<K, V> treeMap = hashMap.entrySet().stream().collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, TreeMap::new));

        return treeMap;
    }



    /**
     * 自定义错误信息输出
     *
     * @param fieldErrorList 参数较验错误列表
     * @return
     */
    public static  String populateFieldErrors(List<FieldError> fieldErrorList) {
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError fieldError : fieldErrorList) {

            errorMessage.append(fieldError.getField()).append("：");
            errorMessage.append(fieldError.getDefaultMessage());
            errorMessage.append(CommonConstant.LOG_SPLIT);

        }
        return errorMessage.toString();
    }
}
