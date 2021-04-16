package com.smk.common;

import com.smk.common.constant.CommonConstant;
import com.smk.common.util.RsaUtil;
import com.smk.common.util.SecureUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static com.smk.common.util.RsaUtil.Private_Key;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/11 15:14
 * Copyright (c) 2019
 */
@RunWith(SpringRunner.class)
public class SecureUtilTest {

    @Test
    public void testMD5Util() {
//        SsoLogOffReq ssoLogOffReq = new SsoLogOffReq();
//        ssoLogOffReq.setReqsn("jkjkj4567811114215");
//        ssoLogOffReq.setUserId("123");
//        ssoLogOffReq.setMobile("456");
//        String key = "9d101c97133837e13dde2d32a5054abb";
//        System.out.println(SecureUtil.getMd5Sign(ssoLogOffReq, key, CommonConstant.LOG_OFF_IGNORE_PARAM));
    }

    @Test
    public void testRSAUtil() throws Exception {
        Map<String, String> map = RsaUtil.createPubKeyAndPriKey(RsaUtil.KEYSIZE_1024);
        String date = "1001000000000040732923029863{\"orderid\":\"1234567890\"}";
        String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAUffW1XpLLUdIHwZlGeyCY50uzqAXLz" +
                "+De2Qpp2PADDkSB3HDa2zEetsib0E3xX+MgazOVX3M8Mn6OMscWTt3L+zDlwX5VE6xnsfaoLmpp0jn0yFM3n0X/vf02aw7tyjL" +
                "/33l2jSRN9KZOx9Fk5rh1Drr/0GWg17Hx2VwfQGVNwIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOce1fOd" +
                "/5WbENEuglFZR3WjGpPM4SCbmCvjn5V7N+FfZY/oGbZbp4kfZDdzBKxLIq9lJxeYdsRCDLvFCmSip3DgyxMouGkEy4RuYylxG0" +
                "/tFUo2eSkQmzJfxg0+LA/RaGFvdzKPIzxhL6HDAlzo0az" +
                "+Pq6hblDwZS97fWg8ZAzjAgMBAAECgYEAvYOYdrIpAJjtekmDzIbDxN9gjK9kLuPHLL3yfpVSJfiehBkS9GhFgXf/KkSjIVBe" +
                "+amdG4kGYjmyzUluE0UYvDQFsjxhNqAm+JlFyiop1TM" +
                "+Xj8NBlnMBVzTf5r1PsTQz3IRb5PVpgNdyoJPprphF4tYPQLzo3XQRyxk8jjQY6kCQQD/V7XFJXJbkc84EGS4u0Ff+m" +
                "/G6M3sbwFJqxvurRqyZJVTUDIoT6GAk3AGx3c5uMrMnZ4v2b7wz2UGxu+F/Ew1AkEA57cpVvZV7NIGk3727jcevPRHWj" +
                "/gA1pwB9y8PXEk1bFlr1CucwaHxk0AhTP64ylH436yXPgd45NPGYnbHmentwJAHpm2UPAR6mNpAAEG8RSP2x66Mo5Hz2PmNZvp9wqLvoFLW/aqbCZO5ofEzyssYKCCvU7OXFGuDlHse4lvvxDjWQJAMikBRvT+dsykZcuHUtwVyFutEolfSfqK4/jRuw9CJQxI8eGqUfYo8DF9Gli9R+2IxWOI4v1HIxuhdOrVx0gzEwJAUAQ210IEu/qYxQO9VGXNSDEnSycdcZNO9JSiwIF8HEM48Yng2sePtzDiTs8JYha9+E7/SMpHKxbHpOabRCu4gA==";
        String sign = "OxXP1ln37xhkR3XtYyx8OjsbbEqSTjIL5z6CYJtl+w/KJ/FKjrGnQoCGFIDAbjDUFzWaS6ImuOS199RxYPXLfA+ko/mRpD" +
                "+WYUBnNANa6kO5sXDrd7pnpepxloW5ozy0e1FHr+rMvisorV1TfvvgfKCcFeC5IK8SitGS+D04sG0=";
        System.out.println(RsaUtil.sign(privateKey, date));
        boolean verify = RsaUtil.verify(date, key, sign);
        System.out.println(verify);
        System.out.println(map.get(Private_Key));
        System.out.println(map.get(RsaUtil.Public_Key));
    }

}
