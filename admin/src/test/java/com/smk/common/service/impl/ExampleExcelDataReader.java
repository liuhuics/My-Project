package com.smk.common.service.impl;

import com.smk.common.service.ExcelReadService;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/25 17:13
 * Copyright (c) 2019
 */
@Component
public class ExampleExcelDataReader extends ExcelReadService {

    /**
     * 提取每一行中需要的数据，构造成为一个结果数据对象
     * <p>
     * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
     *
     * @param row 行数据
     * @return 解析后的行数据对象，行数据错误时返回null
     */
    public ExampleExcelData convertRowToData(Row row) {
        ExampleExcelData resultData = new ExampleExcelData();

        Cell cell;
        int cellNum = 0;
        // 获取姓名
        cell = row.getCell(cellNum++);
        String name = convertCellValueToString(cell);
        resultData.setName(name);
        // 获取年龄
        cell = row.getCell(cellNum++);
        String ageStr = convertCellValueToString(cell);
        if (null == ageStr || "".equals(ageStr)) {
            // 年龄为空
            resultData.setAge(null);
        } else {
            resultData.setAge(Integer.parseInt(ageStr));
        }

        // 获取收入
        cell = row.getCell(cellNum++);

        String salary = convertCellValueToString(cell);
        resultData.setSalary(salary == null ? null : new BigDecimal(salary));

        return resultData;
    }

    @Setter
    @Getter
    public static class ExampleExcelData {
        /**
         * 姓名
         */
        private String name;

        /**
         * 年龄
         */
        private Integer age;

        /**
         * 居住地
         */
        private BigDecimal salary;
    }
}
