package com.smk.common.util;

import com.smk.common.constant.CommonConstant;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {

    /**
     * 1024大小秘钥
     */
    public final static int KEYSIZE_1024 = 1024;

    /**
     * 2048大小密钥
     */
    public final static int KEYSIZE_2048 = 2048;

    public final static String Public_Key = "publicKey";

    public final static String Private_Key = "privateKey";

    public static final String KEY_ALGORITHM = "RSA";

    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * @param keysize KEYSIZE_1024 /KEYSIZE_2048
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> createPubKeyAndPriKey(int keysize) throws NoSuchAlgorithmException {

        //生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);

        //初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(keysize);

        //生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //得到私钥和公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        Map<String, String> map = new HashMap<String, String>();
        map.put(Private_Key, getKeyString(privateKey));
        map.put(Public_Key, getKeyString(publicKey));
        return map;
    }

    /**
     * 得到密钥字符串（经过base64编码）
     *
     * @return
     */
    private static String getKeyString(Key key) {
        byte[] keyBytes = key.getEncoded();
        return new Base64().encodeToString(keyBytes);
    }

    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    private static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = new Base64().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = new Base64().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param enStr     待加密字符串
     * @return Base64 加密字符串
     * @throws Exception
     */
    public static String encrypt(String publicKey, String enStr) throws Exception {
        //Cipher负责完成加密或解密工作，基于RSA
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        //根据公钥，对Cipher对象进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        //加密，结果保存进resultBytes，并返回
        byte[] resultBytes = cipher.doFinal(enStr.getBytes(CommonConstant.DEFAULT_ENCODING));
        return Base64.encodeBase64String(resultBytes);
    }

    /**
     * 私钥 解密
     *
     * @param privateKey 私钥
     * @param deStr      使用Base64加密过的加密字符串
     * @return 字符串
     * @throws Exception
     */
    public static String decrypt(String privateKey, String deStr) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);

        //根据私钥对Cipher对象进行初始化
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));

        //解密并将结果保存进resultBytes
        byte[] decBytes = cipher.doFinal(Base64.decodeBase64(deStr));
        return new String(decBytes, CommonConstant.DEFAULT_ENCODING);
    }

    /**
     * 私钥签名
     *
     * @param privateKey
     * @param data
     * @return
     * @throws Exception
     */
    public static String sign(String privateKey, String data) throws Exception {
        PrivateKey privateK = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes("utf-8"));
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 私钥签名
     *
     * @param privateKey
     * @param data
     * @param type       加签类型
     * @return
     * @throws Exception
     */
    public static String sign(String privateKey, String data, String type) throws Exception {
        PrivateKey privateK = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance(type);
        signature.initSign(privateK);
        signature.update(data.getBytes(CommonConstant.DEFAULT_ENCODING));
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 私钥签名
     *
     * @param privateKey
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] signGetByte(String privateKey, String data) throws Exception {
        PrivateKey privateK = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes(CommonConstant.DEFAULT_ENCODING));
        return signature.sign();
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param publicKey
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        PublicKey publicK = getPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data.getBytes(CommonConstant.DEFAULT_ENCODING));
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param publicKey
     * @param sign
     * @param type      验签类型
     * @return
     * @throws Exception
     */
    public static boolean verify(String data, String publicKey, String sign, String type) throws Exception {
        PublicKey publicK = getPublicKey(publicKey);
        Signature signature = Signature.getInstance(type);
        signature.initVerify(publicK);
        signature.update(data.getBytes(CommonConstant.DEFAULT_ENCODING));
        return signature.verify(Base64.decodeBase64(sign));
    }

}
