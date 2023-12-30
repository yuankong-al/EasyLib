package com.yuankong.easylib.util.timer.cron;

import com.yuankong.easylib.util.timer.cron.tools.CronUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface CronBuilder {
    default void register(Plugin plugin){
        Bukkit.getScheduler().runTask(plugin,()->CronUtil.register(plugin,this));
    }
    default void removeJob(String jobKey){
        CronUtil.removeJob(this,jobKey);
    }

    default void shutdown(){
        CronUtil.shutdown(this);
    }

    //因为通过注解传输cron表达式只能使用常量，当需要使用变量的时候，重写改方法即可
    default Map<String,String> useCronMap(){
        return null;
    }
}
