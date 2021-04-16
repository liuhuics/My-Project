package com.smk.admin.controller;

import com.smk.common.annotation.BizLog;
import com.smk.common.service.ExcelReadService;
import com.smk.common.service.impl.ExampleExcelDataReader;
import com.smk.common.util.FTPUtil;
import com.smk.common.util.JsonUtil;
import com.smk.common.util.MessageUtil;
import com.smk.common.vo.Example;
import com.smk.common.vo.ExampleBody;
import com.smk.common.vo.ExampleResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Project: finance-parent
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/7 16:01
 * Copyright (c) 2019
 */
@RestController
@RequestMapping("/example")
@Slf4j
@Validated
public class ExampleController {
    @Autowired
    private ExcelReadService<ExampleExcelDataReader.ExampleExcelData> exampleExcelDataReader;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    @BizLog("test方法测试")
    public boolean test(String fileName) {
        log.info("进入 test 方法了！");
        Object obj = null;
        System.out.println(obj.toString());
        return FTPUtil.isFileExist("/home/ftpuser", fileName);
    }

    @BizLog("test1方法测试")
    @GetMapping("/test1")
    public String test1(Integer test) {
        log.info("进入 test1 方法了！");
        log.info(test + "");
        //        Object o = null;
        //        log.info(o.toString());

        String msg = MessageUtil.get("900");

        return "suc";
    }

    @PostMapping(value = "/test2")
    public boolean test2(String fileName, String fileName2) {
        log.info("进入 test2 方法了！");

        return FTPUtil.isFileExist("/home/ftpuser", fileName);
    }

    @GetMapping(value = "/test22")
    public boolean test22(@NotNull(message = "fileName不可为空！") String fileName, String fileName2) {
        log.info("进入 test22 方法了！");
        //        if (StringUtils.isEmpty(fileName)) {

        //            throw new ParamInvalidException("参数不正确");
        //        }
        return true;
    }

    @GetMapping(value = "/test3")
    public boolean test3() {
        log.info("进入 test3 方法了！");

        ExampleBody vo = new ExampleBody();
        vo.setPage(1);
        vo.setPageSize(1);
        vo.setDate(new Date());

        // 设置头信息
        //! 找不到 String result = restTemplate.postForObject("http://localhost:18088/report/example/test44", vo, String
        // .class);
        //        String result = restTemplate.postForObject("http://localhost:18088/report/example/test4", vo,
        // String.class);
        //
        //        result = restTemplate.postForObject("http://localhost:18088/report/example/test5?test=1", vo,
        // String.class);
        //
        String result = restTemplate
                .postForObject("http://localhost:18088/admin/example/test6?test=1&test2=2", vo, String.class);
        //
        //        result = restTemplate.getForObject("http://localhost:18088/report/example/test7/5", String.class);

        return true;
    }

    @PostMapping(value = "/test4")
    public String test4(@RequestBody @Valid ExampleBody vo) {
        log.info("进入 test4 方法了！");
        log.error(vo.toString());

        return "suc";
    }

    @PostMapping(value = "/test44")
    public String test44(int page, int pageSize) {
        log.info("进入 test44 方法了！");
        log.error(page + "");

        return "suc";
    }

    @PostMapping(value = "/test5")
    public String test5(int test, @RequestBody ExampleBody vo) {
        log.info("进入 test5 方法了！");
        log.error(vo.toString());

        return "suc";
    }

    @PostMapping(value = "/test6")
    public String test6(int test, @RequestBody ExampleBody vo, int test2) {
        log.info("进入 test6 方法了！");
        log.error(vo.toString());

        return "suc";
    }

    @GetMapping(value = "/test7/{id}")
    public String test7(@PathVariable int id) {
        log.info("进入 test7 方法了！");
        log.error(id + "");

        return "suc";
    }

    @GetMapping(value = "/test8")
    public String test8() {
        log.info("进入 test8 方法了！");
        String result = restTemplate.getForObject("http://localhost:18088/admin/example/test1", String.class);
        log.info(result);

        //直接报错报出去 int result2 = restTemplate.getForObject("http://localhost:18088/report/example/test9", int.class);
        //        log.info(result2+"");

        /*ExportResp exportResp = restTemplate
                .getForObject("http://localhost:18088/report/example/test10", ExportResp.class);

        log.info(exportResp.toString());*/

        ExampleResp example = restTemplate
                .getForObject("http://localhost:18088/admin/example/test11", ExampleResp.class);
        log.info(example.getCode() + "");
        log.info(example.toString());

        //404这时直接抛出错误
        //        UserAccountResp query = restTemplate
        //                .getForObject("http://localhost:18088/report/example/test12?page="+1+"pageSize="+5,
        //                        UserAccountResp.class);
        //        log.info(query.getCode() + "");
        //        log.info(query.toString());

        /*UserAccountResp query = restTemplate
                .getForObject("http://localhost:18088/report/example/list12?page=" + 1 + "&pageSize=" + 5,
                        UserAccountResp.class);
        log.info(query.getCode() + "");
        log.info(query.toString());
        List<UserAccount> userAccounts = query.getPageInfo().getList();
        for (UserAccount userAccount : userAccounts) {
            System.out.println(userAccount.getCredId());
        }*/

        return "suc";
    }

    @GetMapping("/test9")
    public int test9() {
        log.info("进入 test9 方法了！");
        Object o = null;
        log.info(o.toString());
        return 1;
    }


    @GetMapping("/test11")
    public ExampleResp test11() {
        log.info("进入 test11 方法了！");
        ExampleResp example = new ExampleResp();
        example.setCount(1);
        example.setExamples(Arrays.asList(new Example(1)));
        example.setDate(new Date());
        return example;
    }


    @GetMapping(value = "/test13",
            params = "hello=wor")
    public ExampleResp test13() {
        log.info("进入 test13 方法了！");
        ExampleResp example = new ExampleResp();
        example.setCount(1);
        example.setExamples(Arrays.asList(new Example(1)));
        example.setDate(new Date());
        return example;
    }

    @GetMapping(value = "/test13",
            params = "hello=world")
    public ExampleResp test14() {
        log.info("进入 test14 方法了！");
        ExampleResp example = new ExampleResp();
        example.setCount(1);
        example.setExamples(Arrays.asList(new Example(1)));

        return example;
    }

    /**
     * form/data、x-www-form-urlencoded类型的可直接赋值属性
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/test15")
    public String test15(ExampleBody vo) {
        log.info("进入 test15 方法了！");
        log.error(vo.toString());

        return "suc";
    }

    /**
     * json类型的可直接赋值属性
     *
     * @param vo
     * @return
     */
    @PostMapping(value = "/test16")
    public String test16(@RequestBody ExampleBody vo) {
        log.info("进入 test16 方法了！");
        log.error(vo.toString());

        return "suc";
    }

    /**
     * 验证 request中通过getParameter拿不到requestBody中参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/body")
    public ExampleBody body(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            builder.append(line);
            line = reader.readLine();
        }
        reader.close();

        String reqBody = builder.toString();
        ExampleBody exampleBody = JsonUtil.json2Object(reqBody, ExampleBody.class);
        log.info("req: {}, request: {}", exampleBody, request.getParameterMap());
        return exampleBody;
    }

    @PostMapping("/readExcel")
    public List<ExampleExcelDataReader.ExampleExcelData> readExcel(String fileName) {
        List<ExampleExcelDataReader.ExampleExcelData> list = exampleExcelDataReader.readExcel(fileName, false);
        return list;
    }

}
