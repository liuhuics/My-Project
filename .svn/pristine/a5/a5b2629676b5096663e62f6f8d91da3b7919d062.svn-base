package com.smk.common.service.impl;

import com.smk.common.service.ExcelReadService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: zhangwj
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019-11-27
 * Copyright (c) 2019, 96225.com.cn All Rights Reserved.
 */
@Component
public class ActiveExcelDataReader extends ExcelReadService {

    @Override
    public ActiveMobileData convertRowToData(Row row) {
        ActiveMobileData activeMobileData = new ActiveMobileData();

        //获取电话号码，2020-02-11 因需求变更改为es_id
        Cell cell = row.getCell(0);
        String mobile = convertCellValueToString(cell);

        if (StringUtils.isEmpty(mobile)) {
            return null;
        }

       /*//校验电话号码
       Pattern p = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|" +
                "(19[8|9|2]))\\d{8}$");
        Pattern p = Pattern.compile("\\d{11}");
        Matcher m = p.matcher(mobile);
        //若不合法则返回空
        if (!m.matches()) {
            return null;
        }*/

        activeMobileData.setMobile(mobile);

        return activeMobileData;
    }

    @Setter
    @Getter
    public static class ActiveMobileData {
        private String mobile;
    }
}
