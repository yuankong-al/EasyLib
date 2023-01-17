package com.yuankong.easylib.util.timer;

public interface TimerUtil {
    void times(long currentTimeMillis);

    default boolean getCancel(){
        return false;
    }

    default void setCancel(boolean cancel) {

    }

    default void run(long currentTimeMillis){
        times(currentTimeMillis);
    }
}
