package com.yuankong.easylib.util.timer.cron.tools;

import com.yuankong.easylib.util.timer.cron.CronBuilder;
import com.yuankong.easylib.util.timer.cron.CronScheduler;
import org.bukkit.plugin.Plugin;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CronManager {
    Plugin plugin;
    public HashMap<CronScheduler, Method> cronMap = new HashMap<>();
    Scheduler scheduler;
    HashMap<String,CronScheduler> jobKeyMap = new HashMap<>();
    CronBuilder cronBuilder;
    Map<String,String> strCronMap;

    public CronManager(Plugin plugin,CronBuilder cronBuilder) {
        this.plugin = plugin;
        this.cronBuilder = cronBuilder;
        this.strCronMap = cronBuilder.useCronMap();
        if (this.strCronMap == null){
            strCronMap = new HashMap<>();
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }
    public void addCron(CronScheduler scheduler,Method method){
        String jobKey = scheduler.jobKey();
        if (!jobKey.equals("default") && jobKeyMap.containsKey(jobKey)){
            plugin.getLogger().warning(jobKey+"重复的jobKey,方法"+method.getName()+"未生效！！！");
            return;
        }
        cronMap.put(scheduler,method);
        jobKeyMap.put(jobKey,scheduler);
    }

    public HashMap<CronScheduler, Method> getCronMap() {
        return cronMap;
    }

    int i = 0;
    public String getDefaultJobKey(){
        String key = plugin.getName()+ "_"+ this.hashCode() + "_" + i;
        i = i + 1;
        return key;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void removeJob(String jobKey){
        CronScheduler cronScheduler = jobKeyMap.get(jobKey);
        if(cronScheduler == null){
            plugin.getLogger().warning("removeJob - 为找到key" + jobKey+",移除操作失败");
            return;
        }
        CronScheduler remove = jobKeyMap.remove(jobKey);
        cronMap.remove(remove);
        try {
            scheduler.deleteJob(JobKey.jobKey(jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        try {
            boolean shutdown = scheduler.isShutdown();
            if (shutdown){
                return;
            }
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public CronBuilder getCronBuilder() {
        return cronBuilder;
    }

    public Map<String, String> getStrCronMap() {
        return strCronMap;
    }
}
