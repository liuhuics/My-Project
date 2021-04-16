package com.smk.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Description: ftp协议上传下载类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/7 10:21
 * Copyright (c) 2019
 */
@Slf4j
@Component
public class FTPUtil {
    private static final String CONTROL_ENCODING = StandardCharsets.UTF_8.name();

    private static final int CONNECT_TIMEOUT = 1000; // 1秒

    private static final int BUFFER_SIZE = 1024;

    private static String ftpHost;

    private static int ftpPort;

    private static String ftpUsername;

    private static String ftpPassword;

    private static String defaultUploadDir;

    @Value("${ftp.host:localhost}")
    public void setFtpHost(String ftpHost) {
        FTPUtil.ftpHost = ftpHost;
    }

    @Value("${ftp.port:21}")
    public void setFtpPort(int ftpPort) {
        FTPUtil.ftpPort = ftpPort;
    }

    @Value("${ftp.username:admin}")
    public void setFtpUsername(String ftpUsername) {
        FTPUtil.ftpUsername = ftpUsername;
    }

    @Value("${ftp.password:admin}")
    public void setFtpPassword(String ftpPassword) {
        FTPUtil.ftpPassword = ftpPassword;
    }

    @Value("${ftp.defaultUploadDir:/home/ftpuser}")
    public void setDefaultDir(String defaultDir) {
        FTPUtil.defaultUploadDir = defaultDir;
    }

    /**
     * 将流输出到默认ftp路径
     *
     * @param is       输入流
     * @param fileName
     * @return 指定文件名称
     */
    public static boolean uploadWithInputStream2DefaultDir(InputStream is, String fileName) {
        return uploadWithInputStream(defaultUploadDir, is, fileName);
    }

    /**
     * 上传文件到默认ftp路径
     *
     * @param localFilePath
     * @return
     */
    public static boolean uploadFile2DefaultDir(String localFilePath) {
        return uploadFile(defaultUploadDir, localFilePath);
    }

    /**
     * 通过流的方式直接写出到ftp
     *
     * @param remoteDir 目标ftp服务器目录，格式：/home/ftpuser/test
     * @param is        输入流
     * @param fileName  指定文件名称
     * @return
     */
    public static boolean uploadWithInputStream(String remoteDir, InputStream is, String fileName) {
        FTPClient ftp = null;

        try {
            ftp = initFtpClient(remoteDir);
            if (ftp == null) {
                log.error("ftp初始化失败");
                return false;
            }
            boolean storeRet = ftp.storeFile(fileName, is);
            if (!storeRet) {
                log.error("上传文件失败");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("FTP上传文件出错！", e);
            return false;
        } finally {
            close(ftp);
        }
    }

    /**
     * 上传文件
     *
     * @param remoteDir     目标ftp服务器目录，格式：/home/ftpuser/test
     * @param localFilePath 本地文件绝对路径:e:/test/a.txt
     * @return 上传结果 true - 上传成功 false - 上传失败
     */
    public static boolean uploadFile(String remoteDir, String localFilePath) {
        FTPClient ftp = null;

        try {
            ftp = initFtpClient(remoteDir);
            if (ftp == null) {
                log.error("ftp初始化失败");
                return false;
            }
            File file = new File(localFilePath);
            try (InputStream is = new FileInputStream(file)) {
                boolean storeRet = ftp.storeFile(file.getName(), is);
                if (!storeRet) {
                    log.error("上传文件失败");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("FTP上传文件出错！", e);
            return false;
        } finally {
            close(ftp);
        }
    }

    /**
     * 删除文件
     *
     * @param remoteDir      需要删除的文件所有的目录，如：/home/ftpuser/test
     * @param remoteFileName 要删除的文件名，如：tet.txt
     * @return 删除结果<br> true - 删除成功<br>
     * false - 删除失败
     */
    public static boolean delete(String remoteDir, String remoteFileName) {
        FTPClient ftp = null;
        try {
            ftp = initFtpClient(remoteDir);
            if (ftp == null) {
                log.error("ftp初始化失败");
                return false;
            }

            FTPFile[] ftpFiles = ftp.listFiles(remoteFileName);
            if (ftpFiles.length == 0) {
                log.info("文件不存在，删除失败");
                return true;
            }
            boolean deleteRet = ftp.deleteFile(remoteFileName);
            if (!deleteRet) {
                log.error("删除文件失败");
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error("FTP删除文件出错", e);
            return false;
        } finally {
            close(ftp);
        }
    }

    /**
     * 删除文件夹下所有的文件(非文件夹)
     *
     * @param fullDir 待删除目录完整ftp路径，如:/home/ftpuser/test
     * @return 删除结果<br> true - 删除成功<br>
     * false - 删除失败:没有权限也是删除失败
     */
    public static boolean deleteDir(String fullDir) {
        FTPClient ftp = null;
        // fullDir = encode(fullDir);
        try {
            ftp = initFtpClient(fullDir);
            if (ftp == null) {
                log.error("ftp初始化失败");
                return false;
            }

            FTPFile[] ftpFiles = ftp.listFiles();
            if (ftpFiles.length == 0) {
                return true;
            }
            for (FTPFile ftpFile : ftpFiles) {
                if (!ftpFile.isDirectory()) {
                    boolean deleteRet = ftp.deleteFile(ftpFile.getName());
                    if (!deleteRet) {
                        log.error("删除文件失败");
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("FTP删除目录出错", e);
            return false;
        } finally {
            close(ftp);
        }
    }

    /**
     * 移动ftp上的文件
     *
     * @param srcFilePath  原文件绝对路径，如：/home/ftpuser/1.png
     * @param destFilePath 目标文件路径，如：/home/ftpuser/2.png，这里要保证目标文件路径不可多级不存在，即如果/home/ftpuser存在，
     *                     那么目标文件路径可以是/home/ftpuser/test/2.png，但不可以是/home/ftpuser/test/test/2.png
     * @return 操作结果
     */
    public static boolean move(String srcFilePath, String destFilePath) {
        FTPClient ftp = null;
        try {
            ftp = initFtpClient("/");

            // 创建目标文件夹
            String remoteDir = destFilePath.substring(0, destFilePath.lastIndexOf("/"));
            ftp = initFtpClient(remoteDir);

            if (ftp == null) {
                log.error("ftp初始化失败");
                return false;
            }
            if (!ftp.rename(srcFilePath, destFilePath)) {
                log.warn("移动文件失败");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("FTP操作异常", e);
            return false;
        } finally {
            close(ftp);
        }
    }

    /**
     * 下载指定文件到本地
     *
     * @param remoteDir      远程文件所在目录，如：/home/ftpuser
     * @param remoteFileName 远程下载文件名，如:test.txt
     * @param destDir        目标下载地址，如：e:/test
     * @return 下载结果<br> true - 下载成功<br>
     * false - 下载失败
     */
    public static boolean download(String remoteDir, String remoteFileName, String destDir) {
        FTPClient ftp = null;
        try {
            ftp = initFtpClient(remoteDir);
            if (ftp == null) {
                log.error("ftp初始化失败");
                return false;
            }
            try (OutputStream os = new FileOutputStream(destDir + File.separator + remoteFileName)) {
                boolean storeRet = ftp.retrieveFile(remoteFileName, os);
                if (!storeRet) {
                    log.error("下载文件失败");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("FTP下载文件出错", e);
            return false;
        } finally {
            close(ftp);
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param remoteDir      远程目录，如：/home/ftpuser
     * @param remoteFileName 远程文件名称, 如：test.txt
     * @return true文件存在，false文件不存在
     */
    public static boolean isFileExist(String remoteDir, String remoteFileName) {
        FTPClient ftp = null;
        try {
            ftp = initFtpClient(remoteDir);
            if (ftp == null) {
                log.error("ftp初始化失败");
                return false;
            }

            FTPFile[] files = null;
            files = ftp.listFiles(remoteFileName);
            return files.length > 0;

        } catch (Exception e) {
            log.error("FTP操作异常", e);
            return false;
        } finally {
            close(ftp);
        }
    }

    /**
     * 获取文件大小
     *
     * @param remoteDir      远程目录，如：/home/ftpuser
     * @param remoteFileName 远程文件名称, 如：test.txt
     * @return 异常等情况返回-1，其他情况返回正常
     */
    public static long getFileSize(String remoteDir, String remoteFileName) {
        long fileSize = -1;
        FTPClient ftp = null;
        try {
            ftp = initFtpClient(remoteDir);
            if (ftp == null) {
                log.error("ftp初始化失败");
                return fileSize;
            }

            FTPFile[] files = null;
            files = ftp.listFiles(remoteFileName);
            if (files.length > 0) {

                return files[0].getSize();
            } else {
                return fileSize;
            }

        } catch (Exception e) {
            log.error("FTP操作异常", e);
            return fileSize;
        } finally {
            close(ftp);
        }
    }

    /**
     * 初始化ftpClient
     *
     * @param remoteDir
     * @return
     * @throws Exception
     */
    private static FTPClient initFtpClient(String remoteDir) throws Exception {
        FTPClient ftp = new FTPClient();
        // 设置连接超时时间
        ftp.setConnectTimeout(CONNECT_TIMEOUT);
        // 设置传输文件名编码方式
        ftp.setControlEncoding(CONTROL_ENCODING);
        //设置缓冲区大小
        ftp.setBufferSize(BUFFER_SIZE);
        try {
            //                        ftp.connect("192.168.1.122", 21);
            ftp.connect(ftpHost, ftpPort);
        } catch (Exception e) {
            log.error("无法连接FTP");
            return null;
        }
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            log.error("无法连接FTP");
            return null;
        }
        boolean isLoginSuccess = ftp.login(ftpUsername, ftpPassword);
        //                boolean isLoginSuccess = ftp.login("ftpuser", "ftpuser");
        if (!isLoginSuccess) {
            log.error("FTP登录失败");
            return null;
        }
        // 进入被动模式
        ftp.enterLocalPassiveMode();
        boolean changeDirResult = mkAndChangeDir(ftp, remoteDir);
        //        boolean changeDirResult = mkAndChangeAbsoluteDir(ftp, remoteDir);
        if (!changeDirResult) {
            log.error("创建/切换文件夹失败");
            return null;
        }
        // 传输二进制文件
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp;
    }

    /**
     * 如果目录不存在，则创建，但只能创建一级目录，假如仅/home/ftpuser目录存在，如果remoteDir是/home/ftpuser/test/test会无法创建，
     * 这时候文件会上传到/home/ftpuser目录下；如果remoteDir是/home/ftpuser/test，test会创建成功，文件会上传到test目录下
     *
     * @param ftpClient
     * @param remoteDir
     * @return
     * @throws IOException
     */
    private static boolean mkAndChangeDir(FTPClient ftpClient, String remoteDir) throws IOException {

        boolean flag = ftpClient.changeWorkingDirectory(remoteDir);
        if (!flag) {
            //创建上传的路径  该方法只能创建一级目录，在这里如果/home/ftpuser存在则可创建下一级目录
            ftpClient.makeDirectory(remoteDir);
            //指定上传路径
            ftpClient.changeWorkingDirectory(remoteDir);
        }
        return true;

    }

    /**
     * 如果目录不存在，则创建，可兼容多级目录都不存在的情况，即假如仅/home/ftpuser目录存在，如果remoteDir是/home/ftpuser/test/test
     * 也会创建目录成功
     *
     * @param ftpClient
     * @param remoteDir
     * @return
     * @throws IOException
     */
    private static boolean mkAndChangeAbsoluteDir(FTPClient ftpClient, String remoteDir) throws IOException {
        // 切分出所有子文件夹,按顺序
        String[] dirs = remoteDir.split("/");
        // 遍历文件夹
        boolean isrootDir = true;
        for (String subDir : dirs) {
            // 文件夹字符串非空
            if (!subDir.isEmpty()) {
                String dir = subDir;
                if (isrootDir) {
                    dir = "/" + subDir;
                    isrootDir = false;
                }
                // 切换工作目录
                if (!ftpClient.changeWorkingDirectory(dir)) {
                    // 若目录不存在则先创建
                    if (!ftpClient.makeDirectory(dir)) {
                        return false;
                    }
                    // 切换工作目录
                    if (!ftpClient.changeWorkingDirectory(dir)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 在该用户默认目录下创建多级子目录，如ftpuser用户，默认登录目录是/home/ftpuser，如果remoteDir是/test/test，则最终会创建目录
     * /home/ftpuser/test/test目录
     *
     * @param ftpClient
     * @param remoteDir
     * @return
     * @throws IOException
     */
    private static boolean mkAndChangeRelativeDir(FTPClient ftpClient, String remoteDir) throws IOException {
        // 切分出所有子文件夹,按顺序
        String[] dirs = remoteDir.split("/");
        // 遍历文件夹
        for (String subDir : dirs) {
            // 文件夹字符串非空
            if (!subDir.isEmpty()) {
                // 切换工作目录
                if (!ftpClient.changeWorkingDirectory(subDir)) {
                    // 若目录不存在则先创建
                    if (!ftpClient.makeDirectory(subDir)) {
                        return false;
                    }
                    // 切换工作目录
                    if (!ftpClient.changeWorkingDirectory(subDir)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void close(FTPClient ftp) {
        if (ftp != null) {
            try {
                ftp.logout();
            } catch (IOException e) {
                log.error("FTP连接关闭异常", e);
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException e) {
                        log.error("FTP连接关闭异常", e);
                    }
                }
            }
        }
    }

}
