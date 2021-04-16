package com.smk.common;

import com.smk.common.util.ExcelExportUtil;
import com.smk.common.util.FTPUtil;
import com.smk.common.vo.Column2Field;
import com.smk.common.vo.Person;
import com.smk.admin.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //随机生成一个端口
public class UtilTest {


    @Test
    public void testFtpUtil() {
        FTPUtil.uploadFile("/home/ftpuser/teset", "e:/test/OFD_PA_SMK_20190821_02.TXT");
        System.out.println(FTPUtil.getFileSize("/home/ftpuser/", "test.conf"));
        System.out.println(FTPUtil.isFileExist("/home/ftpuser", "test.conf"));
        FTPUtil.download("/home/ftpuser", "test.conf", "e:/test");
        FTPUtil.move("/home/ftpuser/1.png", "/home/ftpuser/2.png");
        FTPUtil.deleteDir("/home/ftpuser/tet");
        System.out.println(FTPUtil.delete("/home/ftpuser/tet", "a.txt"));
    }


    @Test
    public void testExcelUtil() throws FileNotFoundException {
        List<Column2Field> list = Arrays.asList(new Column2Field("姓名", "name"), new Column2Field("年龄", "age"),
                new Column2Field("收入", "salary"));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("e://test.xlsx"));

        List<Person> persons = Arrays.asList(new Person("aswr", 30, new BigDecimal("454554.888")),
                new Person("dfdfd", 35, new BigDecimal("454554.888")));
        //        ExcelExportUtil.exportExcel(list, out, persons);
        //        ExcelExportUtil.exportFile2FtpDefaultDir("测试.xlsx",  list, persons);
        //        ExcelExportUtil.exportFile2SftpDefaultDir("测试.xlsx", list, persons);
        ExcelExportUtil.exportFile2LocalDir("测试.xlsx", list, persons, "e://test");

    }
}
