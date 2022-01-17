package com.atguigu.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import java.util.Date;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/7
 *
 * 任务类
 */

public class HelloQuartz implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        JobDetail detail = jobExecutionContext.getJobDetail();
        String name = detail.getJobDataMap().getString("name");
        System.out.println("my job name is  " + name + " at " + new Date());
    }
}
