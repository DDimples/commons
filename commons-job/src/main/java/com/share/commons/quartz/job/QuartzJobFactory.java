package com.share.commons.quartz.job;

import com.share.commons.quartz.model.ScheduleJob;
import com.share.commons.util.DateUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by 程祥 on 16/8/29.
 * Function：
 */

public class QuartzJobFactory implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("任务成功运行");
        System.out.println("test job "+ System.currentTimeMillis() +"  "+ DateUtil.getToday());
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]");
    }
}
