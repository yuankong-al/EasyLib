package com.yuankong.easylib.util.dragon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DragonPacket {
    String arg();
    String desc() default "";
    String[] packetDesc() default "";
    int delay() default 0;
}
