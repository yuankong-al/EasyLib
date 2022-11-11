package com.yuankong.easylib.api;

import com.yuankong.easylib.config.LoadConfig;

public class GetSQLConfig {
    public static boolean getEnable(){
        return LoadConfig.isEnable();
    }

    public static String getDriver(){
        return LoadConfig.getDriver();
    }

    public static String getUrl(){
        return LoadConfig.getUrl();
    }

    public static String getDatabase(){
        return LoadConfig.getDatabase();
    }

    public static String getParameter(){
        return LoadConfig.getParameter();
    }

    public static String getUsername(){
        return LoadConfig.getUsername();
    }

    public static String getPassword(){
        return LoadConfig.getPassword();
    }
}
