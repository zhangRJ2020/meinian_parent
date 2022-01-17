package com.atguigu.quartz;

import java.util.Date;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/7
 */

// 任务调度类
public class JobDemo {
    // 提供方法（备份数据库，清理日志，清理图片）
    public void run(){
        // 完成业务
        System.out.println(new Date());
    }
}
