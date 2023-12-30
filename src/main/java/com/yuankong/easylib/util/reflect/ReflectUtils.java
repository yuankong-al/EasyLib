package com.yuankong.easylib.util.reflect;

import java.lang.reflect.Field;

public class ReflectUtils {
    public static Object getValue(Class<?> clazz,String variate,Object o){
        Field field;
        try {
            field = clazz.getDeclaredField(variate);
            field.setAccessible(true);
            return field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setValue(Class<?> clazz,String variate,Object o,Object value){
        Field field;
        try {
            field = clazz.getDeclaredField(variate);
            field.setAccessible(true);
            field.set(o,value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
