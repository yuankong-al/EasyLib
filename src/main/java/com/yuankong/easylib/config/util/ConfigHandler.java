package com.yuankong.easylib.config.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
/**
 * 旧版配置文件加载器
 * */
@Deprecated
public abstract class ConfigHandler {
    JavaPlugin plugin;
    HashMap<String, File> files = new HashMap<>();
    HashMap<String, File> folders = new HashMap<>();
    public HashMap<String, YamlConfiguration> configMap = new HashMap<>();
    public HashMap<String, List<YamlConfiguration>> configListMap = new HashMap<>();
    public ConfigHandler(JavaPlugin plugin){
        this.plugin = plugin;
    }
    public ConfigHandler(JavaPlugin plugin, List<String> filePathList){
        this.plugin = plugin;
        for(String str:filePathList){
            addConfigFile(str,false);
        }
        saveConfig();
        loadConfig();
    }
    public ConfigHandler(JavaPlugin plugin, List<String> filePathList, List<String> folderPathList){
        this.plugin = plugin;
        for(String str:filePathList){
            addConfigFile(str,false);
        }
        for(String str:folderPathList){
            addConfigFile(str,true);
        }
        saveConfig();
        loadConfig();
    }

    public void addConfigFile(String filePath,boolean isFolder){
        File file = new File(plugin.getDataFolder(), filePath);
        if (isFolder){
            folders.put(filePath,file);
        }else{
            files.put(filePath,file);
        }
    }

    public void saveConfig(){
        for (String path : files.keySet()) {
            if (!files.get(path).exists()) {
                plugin.saveResource(path, false);
            }
        }
        for (String path : folders.keySet()) {
            if (!folders.get(path).exists()) {
                plugin.saveResource(path, false);
            }
        }
    }

    public void loadConfig() {
        for (String path : files.keySet()) {
            File file = new File(plugin.getDataFolder(), path);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            configMap.put(path,config);
        }
        for (String path : folders.keySet()){
            File folder = new File(plugin.getDataFolder(), path);
            List<YamlConfiguration> list = new ArrayList<>();
            for(File file: Objects.requireNonNull(folder.listFiles())){
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                list.add(config);
            }
            configListMap.put(path,list);
        }
    }
}
