package com.yuankong.easylib.config;

import com.yuankong.easylib.EasyLib;
import org.bukkit.configuration.Configuration;

public class LoadConfig{
    private static boolean enable;
    private static String driver;
    private static String url;
    private static String database;
    private static String parameter;
    private static String username;
    private static String password;
    private static boolean isShutdownManagerWhenClose;
    private static String sendRewardServer;

    public static void load(){
        Configuration config = EasyLib.instance.getConfig();
        enable = config.getBoolean("mysql.enable");
        driver = config.getString("mysql.driver");
        url = config.getString("mysql.url");
        database = config.getString("mysql.database");
        parameter = config.getString("mysql.parameter");
        username = config.getString("mysql.username");
        password = config.getString("mysql.password");
        isShutdownManagerWhenClose = config.getBoolean("mysql.isShutdownManagerWhenClose");
        sendRewardServer = config.getString("other.sendRewardServer");
    }

    public static void reload(){
        EasyLib.instance.reloadConfig();
        load();
    }

    public static boolean isEnable() {
        return enable;
    }

    public static String getDriver() {
        return driver;
    }

    public static String getUrl() {
        return url;
    }

    public static String getDatabase() {
        return database;
    }

    public static String getParameter() {
        return parameter;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static boolean isIsShutdownManagerWhenClose() {
        return isShutdownManagerWhenClose;
    }

    public static String getSendRewardServer() {
        return sendRewardServer;
    }
}
