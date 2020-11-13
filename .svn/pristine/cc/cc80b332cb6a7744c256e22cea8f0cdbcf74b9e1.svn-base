package com.smk.common.service;

import com.smk.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Excel 解析父类
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/25 17:02
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Slf4j
public abstract class ExcelReadService<T extends Object> {

    /**
     * 按文件名称读取:开发环境读取10w，约14s
     *
     * @param fileName 要读取的Excel文件所在绝对路径，如：/home/test/t.xlsx
     * @param failPass 某行出错时，是否要continue，true表示继续处理，false表示退出解析
     * @param <T>
     * @return 读取结果列表，读取失败时返回空列表
     */
    public <T> List<T> readExcel(String fileName, boolean failPass) throws BizException {

        // 获取Excel文件
        File excelFile = new File(fileName);
        if (!excelFile.exists()) {
            String s = "指定的Excel文件不存在！";
            log.error(s);
            throw new BizException(s);
        }

        Workbook workbook = null;
        try {
            // 获取Excel工作簿
            workbook = getWorkbook(excelFile);

            // 读取excel中的数据
            List<T> resultDataList = parseExcel(workbook, failPass);

            return resultDataList;
        } catch (BizException e) {
            String s = "解析Excel失败，文件名：" + excelFile.getName() + " 错误信息：" + e.getMessage();
            log.error(s, e);
            throw new BizException(s);
        } catch (Exception e) {
            String s = "解析Excel失败，文件名：" + excelFile.getName();
            log.error(s, e);
            throw new BizException(s + " 错误信息：" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    /**
     * 读取浏览器中上传的excel文件
     *
     * @param file     浏览器中上传的excel文件
     * @param failPass 某行出错时，是否要continue，true表示继续处理，false表示退出解析
     * @return 读取列表
     */
    public List<T> readExcel(MultipartFile file, boolean failPass) throws BizException {

        // 获取Excel后缀名
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty() || fileName.lastIndexOf(".") < 0) {
            String s = "解析Excel失败，因为获取到的Excel文件名非法！";
            log.error(s);
            throw new BizException(s);
        }

        Workbook workbook = null;
        try {
            // 获取Excel工作簿
            workbook = getWorkbook(file.getInputStream());

            // 读取excel中的数据
            List<T> resultDataList = parseExcel(workbook, failPass);

            return resultDataList;
        } catch (BizException e) {
            String s = "解析Excel失败，文件名：" + file.getName() + " 错误信息：" + e.getMessage();
            log.error(s, e);
            throw new BizException(s);
        } catch (Exception e) {
            String s = "解析Excel失败，文件名：" + file.getName();
            log.error(s, e);
            throw new BizException(s + " 错误信息：" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    /**
     * 解析Excel数据
     *
     * @param workbook Excel工作簿对象
     * @param failPass 某行出错时，是否要continue，true表示继续处理，false表示退出解析
     * @return 解析结果
     */
    private <T> List<T> parseExcel(Workbook workbook, boolean failPass) throws BizException {
        List<T> resultDataList = new ArrayList<>();
        // 解析sheet
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);

            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }

            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                log.warn("解析Excel时，在首行没有读取到任何数据！");
            }

            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {

                Row row = sheet.getRow(rowNum);

                if (null == row) {
                    if (failPass) {
                        continue;
                    }

                    log.error("第 " + (rowNum + 1) + "行数据为空！");
                    throw new BizException("第 " + (rowNum + 1) + "行数据为空！");

                }


                Object resultData = convertRowToData(row);

                if (null == resultData) {
                    if (failPass) {
                        continue;
                    }
                    log.error("第 " + (rowNum + 1) + "行数据不合法！");
                    throw new BizException("第 " + (rowNum + 1) + "行数据不合法！");

                }
                resultDataList.add((T) resultData);
            }
        }

        return resultDataList;
    }

    /**
     * 将单元格内容统一转换为字符串
     *
     * @param cell
     * @return
     */
    public String convertCellValueToString(Cell cell) {
        if (cell == null) {
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();

                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case BLANK:     // 空值
            case ERROR:     // 故障
            default:
                break;
        }
        return returnValue;
    }

    /**
     * 留给具体的业务层去实现，即将每一行转换成自己要的对象
     *
     * @param row
     * @return
     */
    public abstract T convertRowToData(Row row);

    /**
     * 根据文件获取对应的Workbook，兼容 xlsx 和xls后缀
     *
     * @param file
     * @return
     */
    private Workbook getWorkbook(File file) {
        try {
            return WorkbookFactory.create(file);
        } catch (IOException e) {
            log.error("根据文件获取workbook出错！", e);
            throw new BizException("根据文件获取workbook出错:" + e.getMessage());
        }
    }

    /**
     * 根据输入流获取对应的Workbook
     *
     * @param file
     * @return
     */
    private Workbook getWorkbook(InputStream file) {
        try {
            return WorkbookFactory.create(file);
        } catch (IOException e) {
            log.error("根据输入流获取workbook出错！", e);
            throw new BizException("根据输入流获取workbook出错:" + e.getMessage());
        }
    }

}
