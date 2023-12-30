package com.yuankong.easylib.util.economy;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

public class PlayerPointsHandler {

    static PlayerPoints points;
    public static void startPoints(){
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        final Plugin plugin = pluginManager.getPlugin("PlayerPoints");
        if(pluginManager.getPlugin("PlayerPoints") != null && pluginManager.isPluginEnabled(plugin)){
            points = (PlayerPoints) plugin;
        }else{
            points = null;
        }
    }
    public static PlayerPoints getPoints(){
        return points;
    }
    public static PlayerPointsAPI getAPI(){
        return points.getAPI();
    }
    public static boolean hasPoints(UUID uuid,int num){
        return points.getAPI().look(uuid) >= num;
    }
    public static int getAmount(UUID uuid){
        return getAPI().look(uuid);
    }
    public static void takePoints(UUID uuid,int num){
        getAPI().take(uuid,num);
    }
}
