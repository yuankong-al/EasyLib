package com.yuankong.easylib.util.timer;

import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.api.EasyLibApi;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {
    @Override
    public void run() {
        if(!EasyLibApi.timerUtils.isEmpty()){
            EasyLibApi.timerUtils.forEach(((plugin, timerUtils) -> {
                long times = System.currentTimeMillis();
                for(TimerUtil timerUtil: timerUtils){
                    try{
                        timerUtil.run(times);
                    }catch (Exception e){
                        plugin.getLogger().warning(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }));
        }
    }

    public void start(){
        this.runTaskTimerAsynchronously(EasyLib.instance,0,20);
    }
}
