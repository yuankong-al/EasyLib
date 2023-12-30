package com.yuankong.easylib.util.timer;

import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.api.EasyLibApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.DayOfWeek;
import java.time.LocalDateTime;


public abstract class TimerHandler implements TimerUtil{
    boolean cancel = false;
    long laterTime = 0;
    boolean isAsync = true;

    @Override
    public boolean getCancel() {
        return this.cancel;
    }

    @Override
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public void run(long currentTimeMillis) {
        if(!cancel){
            if(currentTimeMillis >= laterTime){
                if (isAsync){
                    times(currentTimeMillis);
                }else{
                    Bukkit.getScheduler().runTask(EasyLib.instance,()->times(currentTimeMillis));
                }
            }
            clock();
        }
    }

    boolean isExecute = true;
    //即使月运行，周(如果达到运行条件)和日也会运行
    public void clock(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime zero = LocalDateTime.of(now.getYear(),now.getMonth(),now.getDayOfMonth(),0,0,2);
        if (now.isBefore(zero)){
            if (isExecute){
                isExecute = false;
                try{
                    everyDayRun();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (now.getDayOfWeek() == DayOfWeek.MONDAY){
                    try{
                        everyWeekRun();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (now.getDayOfMonth() == 1){
                    try{
                        everyMonthRun();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }else{
            isExecute = true;
        }
    }

    public void everyDayRun(){}
    public void everyWeekRun(){}
    public void everyMonthRun(){}

    public long getTimerSleep() {
        return laterTime;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public void timerSleep(long later){
        this.laterTime = System.currentTimeMillis() + later;
    }
    public void register(Plugin plugin){
        EasyLibApi.registerTimer(plugin,this);
    }
}
