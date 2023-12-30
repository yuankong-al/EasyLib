package com.yuankong.easylib.util.timer.cron;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CronScheduler {
    String cron();
    String desc() default "一个builder对应一个schedule，如果remove一个job后还想这个job继续计划，无法再重新添加回来，只能重新register生成一个新的schedule；当cron为字符串'null'时将不会进行注册";
    String jobKey() default "default";
}
