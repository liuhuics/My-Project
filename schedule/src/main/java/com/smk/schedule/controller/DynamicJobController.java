package com.smk.schedule.controller;

import com.smk.common.vo.BaseResult;
import com.smk.common.vo.BaseResultBuilder;
import com.smk.schedule.service.impl.DynamicJobService;
import com.smk.schedule.vo.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 任务动态添加，见http://cxytiandi.com/about
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2019/11/28 10:58
 * Copyright (c) , 96225.com.cn All Rights Reserved.
 */
@RestController
@RequestMapping("/dynamicJob")
@Slf4j
public class DynamicJobController {

    @Autowired
    private DynamicJobService dynamicJobService;

    /**
     * 添加动态任务（适用于脚本逻辑已存在的情况，只是动态添加了触发的时间）
     * 表单方式提交:必填的如下
     * jobName="excelReadTaskExample"
     * jobClass="com.smk.schedule.task.example.ExcelReadTaskExample"
     * cron="0 0/2 * * * ?"
     * jobParameter="你好.xlsx"
     *
     * @param job 任务信息
     * @return
     */
    @PostMapping("/add")
    public BaseResult addJob(Job job) {

        if (StringUtils.isEmpty(job.getJobName())) {
            return BaseResultBuilder.buildBadRequestResult("name is null");
        }

        if (StringUtils.isEmpty(job.getCron())) {
            return BaseResultBuilder.buildBadRequestResult("cron is null");
        }

        if ("SCRIPT".equals(job.getJobType())) {
            if (StringUtils.isEmpty(job.getScriptCommandLine())) {
                BaseResultBuilder.buildBadRequestResult("scriptCommandLine is null");
            }
        } else {
            if (StringUtils.isEmpty(job.getJobClass())) {
                return BaseResultBuilder.buildBadRequestResult("jobClass is null");
            }
        }

        try {
            dynamicJobService.addJob(job);
        } catch (Exception e) {
            log.error("添加任务时出现异常：", e);
            return BaseResultBuilder.buildServerErrorResult(e.getMessage());
        }
        return BaseResultBuilder.buildSuccessResult();
    }

    /**
     * 删除动态注册的任务（只删除注册中心中的任务信息）,其实可以通过控制台删除
     *
     * @param jobName 任务名称
     * @throws Exception
     */
    @GetMapping("/remove")
    public BaseResult removeJob(String jobName) {
        try {
            dynamicJobService.removeJob(jobName);
        } catch (Exception e) {
            log.error("移除任务时出错：", e);
            return BaseResultBuilder.buildServerErrorResult(e.getMessage());
        }
        return BaseResultBuilder.buildSuccessResult();
    }

}
