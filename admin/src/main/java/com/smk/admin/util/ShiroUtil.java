package com.smk.admin.util;

import com.smk.common.model.SysUser;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/10 11:29
 * Copyright (c) 2020
 */
public class ShiroUtil {

    /**
     * MD5加密账号密码
     *
     * @param userName
     * @param password 原始未加密密码
     * @return
     */
    public static String md5(String userName, String password) {
        Md5Hash hash = new Md5Hash(password, ByteSource.Util.bytes(userName), 2);
        return hash.toString();
    }

    /**
     * 获取AES加密串
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getCipherKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecretKey deskey = keygen.generateKey();
        return Base64.encodeToString(deskey.getEncoded());

    }

    /**
     * 断请求是否是ajax
     *
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SysUser sysUser = new SysUser();
        sysUser.setUserName("admin");
        System.out.println(md5("admin", "abc123"));
        System.out.println(getCipherKey());
    }
}
