package com.smk.common.util;

import com.smk.common.annotation.ExcelColumnName;
import com.smk.common.constant.CommonConstant;
import com.smk.common.vo.Column2Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 导出工具类，导出时文件后缀必须.xlsx，即仅针对高版本excel
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/11 10:08
 * Copyright (c) 2019
 */
@Slf4j
public class ExcelExportUtil {

    /**
     * 导出文件到本地目录
     *
     * @param fileName      文件名称，不需要加后缀 .xlsx
     * @param column2Fields 列与属性的对应列表，如：list = Arrays.asList(new Column2Field("姓名", "name"), new Column2Field("年龄",
     *                      *                      "age"),
     * @param dataset       数据列表，dataset = Arrays.asList(new Person("aswr", 30, new BigDecimal("454554.888")),
     *                      new Person("dfdfd", 35, new BigDecimal("454554.888")));
     * @param localDir      本地目录，格式：e://test
     * @param <T>
     * @return
     */
    public static <T> boolean exportFile2LocalDir(String fileName, List<Column2Field> column2Fields, List<T> dataset,
                                                  String localDir) {

        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(
                    new FileOutputStream(localDir + File.separator + fileName + CommonConstant.EXCEL_SUFFIX));
            return exportExcel(column2Fields, out, dataset);
        } catch (FileNotFoundException e) {
            log.error("文件路径不存在", e);
            return false;
        }

    }

    /**
     * 导出表格到sftp默认路径
     *
     * @param fileName      文件名称，不需要加后缀 .xlsx
     * @param column2Fields 列与属性的对应列表，如：list = Arrays.asList(new Column2Field("姓名", "name"), new Column2Field("年龄",
     *                      *                      "age"),
     * @param dataset       数据列表，dataset = Arrays.asList(new Person("aswr", 30, new BigDecimal("454554.888")),
     *                      new Person("dfdfd", 35, new BigDecimal("454554.888")));
     * @return
     */
    /*public static <T> boolean exportFile2SftpDefaultDir(String fileName, List<Column2Field> column2Fields,
                                                        List<T> dataset) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        boolean isExportSuccess = exportExcel(column2Fields, os, dataset);
        if (!isExportSuccess) {
            log.error("导出文件" + fileName + ".xlsx 时出错，详见异常堆栈信息！");
            return false;
        }
        byte[] b = os.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        return czBankSFTPService.uploadWithInputStream2DefaultDir(in, fileName + CommonConstant.EXCEL_SUFFIX);
    }*/

    /**
     * 导出表格到ftp默认路径
     *
     * @param fileName      文件名称，后缀必须是 .xlsx
     * @param column2Fields 列与属性的对应列表，如：list = Arrays.asList(new Column2Field("姓名", "name"), new Column2Field("年龄",
     *                      *                      "age"),
     * @param dataset       数据列表，dataset = Arrays.asList(new Person("aswr", 30, new BigDecimal("454554.888")),
     *                      new Person("dfdfd", 35, new BigDecimal("454554.888")));
     * @return
     */
    public static <T> boolean exportFile2FtpDefaultDir(String fileName, List<Column2Field> column2Fields,
                                                       List<T> dataset) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        boolean isExportSuccess = exportExcel(column2Fields, os, dataset);
        if (!isExportSuccess) {
            log.error("导出文件" + fileName + " .xlsx 时出错，详见异常堆栈信息！");
            return false;
        }
        byte[] b = os.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        return FTPUtil.uploadWithInputStream2DefaultDir(in, fileName + CommonConstant.EXCEL_SUFFIX);
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符合一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param column2Fields 列与属性的对应列表，如：list = Arrays.asList(new Column2Field("姓名", "name"), new Column2Field("年龄",
     *                      "age"),
     *                      new Column2Field("收入", "salary"));
     * @param out           与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param dataset       数据列表，dataset = Arrays.asList(new Person("aswr", 30, new BigDecimal("454554.888")),
     *                      new Person("dfdfd", 35, new BigDecimal("454554.888")));
     * @return 导出是否成功
     */
    public static <T> boolean exportExcel(List<Column2Field> column2Fields, OutputStream out, List<T> dataset) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SXSSFWorkbook workbook = new SXSSFWorkbook(); //默认100行在内存中，低内存占用版本
        // 生成一个表格
        Sheet sheet = workbook.createSheet();

        CellStyle titleStyle = getTitleStyle(workbook);
        CellStyle cellStyle = getCellStyle(workbook);

        // 产生表格标题行
        Row row = sheet.createRow(0);
        for (int i = 0; i < column2Fields.size(); i++) {
            Column2Field column2Field = column2Fields.get(i);
            Cell cell = row.createCell(i);
            cell.setCellStyle(titleStyle);
            XSSFRichTextString text = new XSSFRichTextString(column2Field.getColname());
            cell.setCellValue(text);
            //            sheet.autoSizeColumn(i);
        }
        //冻结第一行
        sheet.createFreezePane(0, 1, 0, 1);

        sheet.setDefaultColumnWidth(15);
        // 遍历集合数据，产生数据行
        try {
            Iterator<T> it = dataset.iterator();
            int index = 0;
            while (it.hasNext()) {
                index++;
                row = sheet.createRow(index);
                T t = it.next();// 取出每个对象

                int cellIndex = 0;// Excel列索引

                for (Column2Field column2Field : column2Fields) {

                    Cell cell = row.createCell(cellIndex);
                    cell.setCellStyle(cellStyle);

                    String fieldName = column2Field.getFieldName();// 取属性名

                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    //暂不兼容某些方法不存在的情况
                    Method getMethod = null;
                    //                    try {
                    getMethod = dataset.get(0).getClass().getMethod(getMethodName, new Class[]{});
                    //                    } catch (NoSuchMethodException e) {
                    //                        log.error("方法" + getMethodName + "不存在！");
                    //                        continue;
                    //                    }
                    Object value = getMethod.invoke(t);
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value == null) {

                        textValue = "";

                    } else if (value instanceof Date) {

                        textValue = DateFormatUtils.format((Date) value, DateUtil.FULL_TIME_FORMAT);
                    } else {

                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();

                    }
                    // 全部当成String处理
                    //                    XSSFRichTextString richString = new XSSFRichTextString(textValue);
                    cell.setCellValue(textValue);
                    // 清理资源
                    cellIndex++;
                }

            }
            workbook.write(out);
            stopWatch.stop();
            log.info("导出完成，共导出：" + index + " 条，用时：" + stopWatch.getTime(TimeUnit.SECONDS) + " s");
            return true;
        } catch (Exception e) {
            log.error("导出文件时出错！", e);
            return false;
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 表格内容样式
     *
     * @param workbook
     * @return
     */
    private static CellStyle getCellStyle(Workbook workbook) {
        // 生成并设置另一个样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 生成另一个字体
        Font cellFont = workbook.createFont();
        cellFont.setBold(false);

        // 把字体应用到当前的样式
        cellStyle.setFont(cellFont);
        return cellStyle;
    }

    /**
     * 标题行样式
     *
     * @param workbook
     * @return
     */
    private static CellStyle getTitleStyle(Workbook workbook) {
        // 生成一个样式
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 生成一个字体
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);

        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 获取不同类导出的结构:根据属性上的注解获取列名
     *
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> List<Column2Field> getColumn2Fields(Class<T> clz) {
        List<Column2Field> column2Fields = new ArrayList<>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumnName excelColumnName = field.getAnnotation(ExcelColumnName.class);
            if (excelColumnName == null || excelColumnName.value().isEmpty()) {
                continue;
            }
            column2Fields.add(new Column2Field(excelColumnName.value(), field.getName()));
        }

        return column2Fields;
    }

    /**
     * 根据目录和文件名获取文件绝对路径
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static String getExcelFilePath(String dir, String fileName) {
        return dir + fileName + CommonConstant.EXCEL_SUFFIX;

    }

}
