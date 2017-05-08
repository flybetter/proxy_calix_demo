package com.cn.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * Created By Bingyu wu
 * Date: 2017/4/19
 * Time: 下午5:23
 */
public class HelloQuartzJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Hello, Quartz! - executing its JOB at "+new Date() + " by " + context.getTrigger().getCalendarName());
    }
}
