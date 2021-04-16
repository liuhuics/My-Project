package com.smk.common.util;

import com.smk.common.constant.CommonConstant;
import com.smk.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 加解密相关
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/1/6 10:31
 * Copyright (c) 2020
 */
@Slf4j
public class SecureUtil {


    /**
     * 根据封装的对象和密钥获取md5加密的字符串
     *
     * @param t           封装请求的类
     * @param key         密钥
     * @param ignoreField 需要忽略的字段
     * @param <T>
     * @return
     */
    public static <T> String getMd5Sign(T t, String key, String ignoreField) {
        String src = assembleSortString(t, ignoreField);
        src += CommonConstant.AMPERSAND + CommonConstant.LOG_OFF_KEY_PARAM + CommonConstant.EQUAL + key;

        // 拿到一个MD5转换器
        MessageDigest messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messagedigest.digest(src.getBytes(CommonConstant.DEFAULT_ENCODING));

            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            log.error("获取md5算法时出错！", e);
            throw new BizException("获取md5算法时出错！");
        } catch (Exception e) {
            log.error("md5获取加密字符串时出错！", e);
            throw new BizException("md5获取加密字符串时出错!");
        }


    }

    /**
     * 将字节数据转为16进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        // new一个字符数组，这个就是用来组成结果字符串的（一个byte是八位二进制，也就是2位十六进制字符）
        char[] resultCharArray = new char[byteArray.length * 2];

        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            //高4位
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            //低4位
            resultCharArray[index++] = hexDigits[b & 0xf];

        }
        return new String(resultCharArray);
    }


    /**
     * 将类中属性升序拼接成字符串
     *
     * @param t
     * @param ignoreField 需要忽略的属性
     * @param <T>
     * @return
     */
    public static <T> String assembleSortString(T t, String ignoreField) {
        TreeMap<String, String> tree = new TreeMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (ignoreField != null && ignoreField.equals(name)) {
                continue;
            }
            String value = "";
            try {
                value = (String) field.get(t);
            } catch (IllegalAccessException e) {
                String errMsg = "获取属性" + field + "的值时出错：";
                log.error(errMsg, e);
                throw new BizException(errMsg);

            }
            if (StringUtils.isNotEmpty(value)) {
                tree.put(name, value);
            }
        }
        if (tree.size() == 0) {
            return "";
        }
        return getSortedString(tree);

    }

    /**
     * 将treeMap拼接成字符串
     *
     * @param tree
     * @return
     */
    private static String getSortedString(TreeMap<String, String> tree) {
        Iterator<Map.Entry<String, String>> it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            sf.append(en.getKey() + CommonConstant.EQUAL + en.getValue()
                    + CommonConstant.AMPERSAND);
        }
        return sf.substring(0, sf.length() - 1);
    }

}
