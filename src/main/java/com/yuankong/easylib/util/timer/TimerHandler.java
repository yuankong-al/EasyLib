package com.yuankong.easylib.util.timer;

public abstract class TimerHandler implements TimerUtil{
    boolean cancel = false;
    long laterTime = 0;

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
                times(currentTimeMillis);
            }
        }
    }

    public void timerSleep(long later){
        this.laterTime = System.currentTimeMillis() + later;
    }
}
