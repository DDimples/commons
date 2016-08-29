package com.share.commons.web.controller;

import com.share.commons.SpringContext;
import com.share.commons.cache.util.CacheTool;
import com.share.commons.quartz.job.QuartzJobFactory;
import com.share.commons.quartz.model.ScheduleJob;
import com.share.commons.util.DateUtil;
import com.share.commons.util.StringUtil;
import com.share.commons.web.service.GoodsService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 程祥 on 16/7/9.
 * Function：
 */
@Controller
public class IndexController {

    private String regin = "commons";

    @RequestMapping(value = "/index")
    @ResponseBody
    public Object index(HttpServletRequest request){
        return "index";
    }

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/dao")
    @ResponseBody
    public Object dao(HttpServletRequest request){
        try {
            return goodsService.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Exception";
    }


    @RequestMapping(value = "/cache")
    @ResponseBody
    public Object cache(HttpServletRequest request){
        String cacheKey = request.getParameter("key");
        if(StringUtil.isEmpty(cacheKey)){
            return "key can't be null ";
        }
        String configkey = request.getParameter("configkey")==null?"test":request.getParameter("configkey");
        String value = request.getParameter("value")==null?"default":request.getParameter("value");
        Object cache = CacheTool.getObjectFromCache(regin,configkey,cacheKey);
        if(cache!=null){
            return cache;
        }else {
            CacheTool.setObject2Cache(regin,configkey,cacheKey,value);
            Object res = CacheTool.getObjectFromCache(regin,configkey,cacheKey);
            System.out.println(res);
            return res;
        }
    }

    @RequestMapping(value = "/testReqIp")
    @ResponseBody
    public Object testReqIp(HttpServletRequest httpRequest){
        System.out.println(httpRequest.getRemoteAddr());
        System.out.println(httpRequest.getRemoteHost());

        return "testReqIp";
    }




    @RequestMapping(value = "/testQuartz")
    @ResponseBody
    public Object testQuartz(HttpServletRequest httpRequest,String jobId,String jobName) throws SchedulerException {

        Scheduler scheduler = SpringContext.getBean("schedulerFactory");

        ScheduleJob job = new ScheduleJob();
        job.setJobId("10001"+jobId);
        job.setJobName("data_import"+jobName);
        job.setJobGroup("dataWork");
        job.setJobStatus("1");
        job.setCronExpression("0/5 * * * * ?");
        job.setDesc("数据导入任务");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(),job.getJobGroup());
        //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //不存在，创建一个
        if (null == trigger) {
            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
                    .withIdentity(job.getJobName(), job.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                    .getCronExpression());
            //按新的cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                    .getCronExpression());
            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }

        return "testQuartz";
    }

}
