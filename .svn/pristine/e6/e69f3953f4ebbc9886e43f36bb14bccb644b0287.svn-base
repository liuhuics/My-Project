package com.smk.schedule.util;

import com.smk.common.model.CommonMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class SendMsgUtil {

    private static String channo;

    private static String srvid;

    private static String branchno;

    private static String userno;

    private static String password;

    private static String ip;

    private static String port;

    @Value("${sendMsg.sms.channo}")
    public void setChanno(String channo) {
        SendMsgUtil.channo = channo;
    }

    @Value("${sendMsg.sms.srvid}")
    public void setSrvid(String srvid) {
        SendMsgUtil.srvid = srvid;
    }

    @Value("${sendMsg.sms.branchno}")
    public void setBranchno(String branchno) {
        SendMsgUtil.branchno = branchno;
    }

    @Value("${sendMsg.sms.userno}")
    public void setUserno(String userno) {
        SendMsgUtil.userno = userno;
    }

    @Value("${sendMsg.sms.password}")
    public void setPassword(String password) {
        SendMsgUtil.password = password;
    }

    @Value("${sendMsg.sms.ip}")
    public void setIp(String ip) {
        SendMsgUtil.ip = ip;
    }

    @Value("${sendMsg.sms.port}")
    public void setPort(String port) {
        SendMsgUtil.port = port;
    }

    /**
     * 发送短信
     *
     * @param mobile
     * @param content
     */
    public static void sendMsg(String mobile, String content) throws Exception {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content)) {
            return;
        }
        try {
            //拼接短信报文
            String message;
            message = "404000<hsiss><channo>" + channo + "</channo><srvid>" + srvid + "</srvid><branchno>" + branchno + "</branchno><immedflag>1</immedflag>"
                    + "<lastsndtime>0</lastsndtime><userno>" + userno + "</userno><password>" + password + "</password><objaddr>"
                    + mobile + "</objaddr><msgcont>" + content + "</msgcont></hsiss>";
            message = "0" + message.getBytes("GBK").length + message;
            log.info("发送短信报文：" + message);
            String returnMsg = sendMsg(message, ip, port);
            log.info("短信返回报文：" + returnMsg);
        } catch (Exception e) {
            log.error("短信发送失败：" + e);
            throw new Exception("短信发送失败：" + e);
        }
    }

    /**
     * 短信模板拼装
     *
     * @param param
     * @param commonMsgTemplate
     */
    public static void assembledMsg(Map<String, Object> param, CommonMsgTemplate commonMsgTemplate) {
        String content = commonMsgTemplate.getMsgContent();
        Pattern pattern = Pattern.compile("\\{\\w*}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String mkey = matcher.group();
            String key = mkey.substring(1, mkey.length() - 1);
            Object rpcValue = param.get(key);
            content = content.replaceFirst("\\{\\w*}", null == rpcValue ? "CANNOT FIND KEY:" + key : String.valueOf(rpcValue));
        }
        commonMsgTemplate.setMsgContent(content);
    }

    private static String sendMsg(String info, String ip, String port) {
        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, Integer.parseInt(port));
            socket.connect(socketAddress, 30 * 1000);
            socket.setSoTimeout(30 * 1000);
            os = socket.getOutputStream();
            os.write(info.getBytes("GBK"));
            byte[] bytes = new byte[100];
            is = socket.getInputStream();
            is.read(bytes);
            return new String(bytes, "GBK");
        } catch (Exception e) {
            log.error("Socket调用接口异常", e);
            return "";
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception ioException) {
                log.error("关闭资源异常", ioException);
            }
        }

    }
}
