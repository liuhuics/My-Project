package com.smk.common.service;

import com.jcraft.jsch.*;
import com.smk.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * @Description: sftp交互用基类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/3/23 14:46
 * Copyright (c) 2020, 96225.com.cn All Rights Reserved.
 */
@Slf4j
public abstract class SFTPService {

    public static final int TIMEOUT = 100000;

    protected String sftpHost;

    protected int sftpPort;

    protected String sftpUsername;

    protected String sftpPassword;

    protected String sftpDefaultUploadDir;

    protected String privateKeyPath;

    protected String privateKeyPwd;

    /**
     * 判断文件或目录是否存在
     *
     * @param remoteFilePath 远程文件或目录，如/usr/test或 /usr/test/a.txt
     * @return
     */
    public boolean isFileExists(String remoteFilePath) {
        log.info(getBasicLogStr(this.sftpHost, remoteFilePath));

        boolean found = false;
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = getSession();
            channelSftp = getChannel(session);
            if (channelSftp == null) {
                throw new BizException("获取连接时出错！" + getBasicLogStr(this.sftpHost, null));
            }
        } catch (Exception e) {
            log.error("获取连接时出错！" + getBasicLogStr(this.sftpHost, null), e);
            throw new BizException("获取连接时出错！" + getBasicLogStr(this.sftpHost, null));
        }

        SftpATTRS attributes = null;
        try {
            attributes = channelSftp.stat(remoteFilePath);
        } catch (Exception e) {
            log.error("判断文件是否存在时出错！" + getBasicLogStr(this.sftpHost, remoteFilePath));
            found = false;
        } finally {
            closeChannel(channelSftp);
            closeSession(session);
        }
        if (attributes != null) {
            found = true;
        }
        return found;
    }


    /**
     * 上传文件到默认路径
     *
     * @param localFilePath 需要上传的本地文件的绝对路径，格式：e:/test/a.txt
     * @return
     */
    public boolean uploadFile2DefaultDir(String localFilePath) {
        return uploadFile(sftpDefaultUploadDir, localFilePath);
    }

    /**
     * 将输入流写出到默认目录
     *
     * @param in       输入流
     * @param fileName 指定文件名称
     * @return
     */
    public boolean uploadWithInputStream2DefaultDir(InputStream in, String fileName) {
        return uploadWithInputStream(sftpDefaultUploadDir, in, fileName);
    }

    /**
     * 将输入流写出到指定目录
     *
     * @param sftpFolder 目标sftp服务器的目录,格式: /ftp/path
     * @param in         输入流
     * @param fileName   指定文件名称
     * @return
     */
    public boolean uploadWithInputStream(String sftpFolder, InputStream in, String fileName) {
        log.info(getBasicLogStr(this.sftpHost, sftpFolder));
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = getSession();
            channelSftp = getChannel(session);
            if (channelSftp == null) {
                return false;
            }
            channelSftp.cd(sftpFolder);

            channelSftp.put(in, fileName);

            //            file.delete();
            return true;

        } catch (Exception e) {
            log.error("上传文件时出错！" + getBasicLogStr(this.sftpHost, null), e);
            return false;
        } finally {
            closeChannel(channelSftp);
            closeSession(session);
        }
    }

    /**
     * 上传指定文件到sftp服务器
     *
     * @param sftpFolder    目标sftp服务器的目录,格式: /ftp/path
     * @param localFilePath 需要上传的本地文件的绝对路径，格式：e:/test/a.txt
     * @return
     */
    public boolean uploadFile(String sftpFolder, String localFilePath) {
        log.info(getBasicLogStr(this.sftpHost, sftpFolder));
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = getSession();
            channelSftp = getChannel(session);
            if (channelSftp == null) {
                return false;
            }
            channelSftp.cd(sftpFolder);
            File file = new File(localFilePath);

            channelSftp.put(new FileInputStream(file), file.getName());

            //            file.delete();
            return true;

        } catch (Exception e) {
            log.error("上传文件时出错！" + getBasicLogStr(this.sftpHost, null), e);
            return false;
        } finally {
            closeChannel(channelSftp);
            closeSession(session);
        }
    }

    /**
     * 下载某目录下所有文件
     *
     * @param sftpFolder SFTP服务器中文件所在目录,格式: /ftp/path
     * @param localPath  下载到本地的目录,格式: e:/download
     * @return 文件列表
     */
    public String[] downloadFiles(String sftpFolder, String localPath) {
        log.info(getBasicLogStr(this.sftpHost, sftpFolder));
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = getSession();
            channelSftp = getChannel(session);
            if (channelSftp == null) {
                return null;
            }
        } catch (Exception e) {
            log.error("获取连接时出错！" + getBasicLogStr(this.sftpHost, null), e);
            return null;
        }

        String[] fileList = null;
        try {
            channelSftp.cd(sftpFolder);
            fileList = lsFile(channelSftp);
            File localFoler = new File(localPath);
            if (!localFoler.exists()) {
                localFoler.mkdirs();
            }
            for (String remoteFile : fileList) {
                channelSftp.get(remoteFile, localPath);
            }
        } catch (Exception e) {
            log.error("sftp 下载文件出错！" + getBasicLogStr(this.sftpHost, null), e);
            return null;
        } finally {
            closeChannel(channelSftp);
            closeSession(session);
        }

        return fileList;
    }

    /**
     * 下载指定文件
     *
     * @param sftpFilePath SFTP服务器中文件的绝对路径,格式: /ftp/path/a.txt
     * @param localPath    下载到本地的目录,格式: e:/download
     * @return 下载是否成功
     */
    public boolean downloadFile(String sftpFilePath, String localPath) {
        log.info(getBasicLogStr(this.sftpHost, sftpFilePath));
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = getSession();
            channelSftp = getChannel(session);
            if (channelSftp == null) {
                return false;
            }
        } catch (Exception e) {
            log.error("获取连接时出错！" + getBasicLogStr(this.sftpHost, null), e);
            return false;
        }
        try {
            String remoteFilePath = sftpFilePath.substring(0, sftpFilePath.lastIndexOf("/"));
            File localFoler = new File(localPath);
            if (!localFoler.exists()) {
                localFoler.mkdirs();
            }
            channelSftp.cd(remoteFilePath);
            channelSftp.get(sftpFilePath, localPath);
            return true;
        } catch (Exception e) {
            log.error("下载文件出错！" + getBasicLogStr(this.sftpHost, null), e);
            return false;
        } finally {
            closeChannel(channelSftp);
            closeSession(session);
        }
    }

    /**
     * 枚举，用于过滤文件和文件夹
     */
    private enum Filter {
        /**
         * 文件及文件夹
         */
        ALL,
        /**
         * 文件
         */
        FILE,
        /**
         * 文件夹
         */
        DIR
    }

    ;

    /**
     * 判断是否是否过滤条件
     *
     * @param entry  LsEntry
     * @param filter 过滤参数
     * @return boolean
     */
    private boolean filter(ChannelSftp.LsEntry entry, Filter filter) {
        if (filter.equals(Filter.ALL)) {
            return !entry.getFilename().equals(".") && !entry.getFilename().equals("..");
        } else if (filter.equals(Filter.FILE)) {
            return !entry.getFilename().equals(".") && !entry.getFilename().equals("..") && !entry.getAttrs().isDir();
        } else if (filter.equals(Filter.DIR)) {
            return !entry.getFilename().equals(".") && !entry.getFilename().equals("..") && entry.getAttrs().isDir();
        }
        return false;
    }

    /**
     * 当前目录下文件名称列表
     *
     * @return String[]
     */
    public String[] lsFile(ChannelSftp channel) {
        return listFile(channel, Filter.FILE);
    }

    /**
     * 当前目录下文件夹名称列表
     *
     * @return String[] 结果列表
     */
    public String[] lsDir(ChannelSftp channel) {
        return listFile(channel, Filter.DIR);
    }

    /**
     * 列出当前通过所指目录下的文件及文件夹
     *
     * @param filter 过滤参数
     * @return String[] 结果列表
     */
    private String[] listFile(ChannelSftp channel, Filter filter) {
        log.info(getBasicLogStr(this.sftpHost, null));
        Vector<ChannelSftp.LsEntry> list = null;
        try {
            list = channel.ls(channel.pwd());
        } catch (SftpException e) {
            log.error("can not list directory！" + getBasicLogStr(this.sftpHost, null), e);
            return new String[0];
        }

        List<String> resultList = new ArrayList<String>();
        for (ChannelSftp.LsEntry entry : list) {
            if (filter(entry, filter)) {
                resultList.add(entry.getFilename());
            }
        }
        return resultList.toArray(new String[0]);
    }

    /**
     * 根据session 打开通道
     *
     * @param session 已连接的session
     * @return 通道
     */
    private ChannelSftp getChannel(Session session) {

        try {
            Channel channel = session.openChannel("sftp"); // 打开SFTP通道
            channel.connect(); // 建立SFTP通道的连接
            return (ChannelSftp) channel;
        } catch (Exception e) {
            log.error("获取sftp连接错误!", e);
            return null;
        }
    }

    /**
     * 根据某地址获取连接
     *
     * @return 建立成功的session
     * @throws JSchException
     */
    private Session getSession() throws JSchException {
        JSch jsch = new JSch(); // 创建JSch对象

        // 设置密钥和密码
        if (StringUtils.isNotBlank(this.privateKeyPath)) {// 设置不带口令的密钥
            if (StringUtils.isNotBlank(this.privateKeyPwd)) {// 设置带口令的密钥
                jsch.addIdentity(privateKeyPath, privateKeyPwd);
            } else {
                jsch.addIdentity(privateKeyPath);
            }
        }

        // 根据用户名，主机ip，端口获取一个Session对象
        //        Session session = jsch.getSession("root", "192.168.1.122", 22);
        Session session = jsch.getSession(sftpUsername, sftpHost, sftpPort);

        session.setPassword(sftpPassword); // 设置密码


        //        session.setPassword("534261java"); // 设置密码
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties

        session.setTimeout(TIMEOUT); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        return session;
    }

    /**
     * 关闭channel
     *
     * @throws Exception
     */
    private void closeChannel(ChannelSftp channel) {
        if (channel != null) {
            channel.disconnect();
        }

    }

    /**
     * 关闭session
     *
     * @param session
     */
    private void closeSession(Session session) {
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * 获取日志打印内容
     *
     * @param ip
     * @param folder
     * @return
     */
    private String getBasicLogStr(String ip, String folder) {
        return StringUtils.isEmpty(folder) ? "ip：" + ip + " " : "ip：" + ip + "，远程文件：" + folder;
    }

}


