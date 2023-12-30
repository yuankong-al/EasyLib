package com.yuankong.easylib.util.timer.cron.tools;

import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.util.timer.cron.CronBuilder;
import com.yuankong.easylib.util.timer.cron.CronScheduler;
import org.bukkit.plugin.Plugin;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CronUtil implements Job {
    public static HashMap<CronBuilder,CronManager> cronManagers = new HashMap<>();
    public static void register(Plugin plugin, CronBuilder cronBuilder){
        CronManager cronManager = cronManagers.get(cronBuilder);
        if (cronManager != null){
            plugin.getLogger().warning("manager已存在，请移除后再注册！！！");
            return;
        }
        cronManager = new CronManager(plugin,cronBuilder);
        for (Method method : cronBuilder.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(CronScheduler.class)) {
                cronManager.addCron(method.getAnnotation(CronScheduler.class), method);
            }
        }
        cronManagers.put(cronBuilder,cronManager);
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            for (Map.Entry<CronScheduler, Method> kv : cronManager.getCronMap().entrySet()) {
                CronScheduler cronScheduler = kv.getKey();
                Method method = kv.getValue();
                String cron = cronScheduler.cron();
                if (cron.equals("null")){
                    continue;
                }
                //解析cron
                CronScheduleBuilder strCron = getCronFromString(cron);
                if (strCron == null){
                    String str = cronManager.strCronMap.get(cron);
                    if (str == null){
                        cronManager.plugin.getLogger().warning("cron表达式["+cron+"]无效!!!");
                        continue;
                    }
                    if (str.equals("null")){
                        continue;
                    }
                    strCron = getCronFromString(str);
                    if (strCron == null){
                        cronManager.plugin.getLogger().warning("cron表达式["+str+"]无效!!!");
                        continue;
                    }
                }
                String jobKey = cronScheduler.jobKey();
                if (jobKey.equals("default")){
                    jobKey = cronManager.getDefaultJobKey();
                }
                //创建job
                JobDetail jobDetail = JobBuilder.newJob(CronUtil.class).withIdentity(jobKey).build();
                //向job里面存入数据
                jobDetail.getJobDataMap().put("CronSchedulerMethod",method);
                //创建触发器
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobKey+"_Trigger")
                        .withSchedule(strCron)
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
            //启动
            scheduler.start();
            cronManager.setScheduler(scheduler);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        Method method = (Method) jobDetail.getJobDataMap().get("CronSchedulerMethod");
        try {
            method.invoke(CronBuilder.class);
        }catch (IllegalAccessException | InvocationTargetException ex2) {
            ex2.printStackTrace();
        }
    }

    public static void removeJob(CronBuilder cronBuilder,String jobKey){
        CronManager cronManager = cronManagers.get(cronBuilder);
        if (cronManager == null){
            EasyLib.instance.getLogger().warning("未能根据"+cronBuilder+"找到manager，移除job"+jobKey+"失败！！！");
            return;
        }
        cronManager.removeJob(jobKey);
    }
    public static void shutdown(CronBuilder cronBuilder){
        CronManager cronManager = cronManagers.get(cronBuilder);
        if (cronManager == null){
            EasyLib.instance.getLogger().warning("未能根据"+cronBuilder+"找到manager，shutdown失败！！！");
            return;
        }
        cronManager.shutdown();
        cronManagers.remove(cronBuilder);
    }

    public static CronScheduleBuilder getCronFromString(String cron){
        try{
            return CronScheduleBuilder.cronSchedule(cron);
        }catch (RuntimeException e){
            return null;
        }
    }
}
