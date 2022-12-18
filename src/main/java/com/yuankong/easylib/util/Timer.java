package com.yuankong.easylib.util;

import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.api.EasyLibApi;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {
    @Override
    public void run() {
        if(!EasyLibApi.timerUtils.isEmpty()){
            EasyLibApi.timerUtils.forEach(((plugin, timerUtils) -> {
                for(TimerUtil timerUtil: timerUtils){
                    try{
                        timerUtil.times();
                    }catch (Exception e){
                        plugin.getLogger().warning(e.getMessage());
                    }
                }
            }));
        }

    }

    public void start(){
        this.runTaskTimerAsynchronously(EasyLib.instance,0,20);
    }


}
