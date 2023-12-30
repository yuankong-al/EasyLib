package com.yuankong.easylib.config.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigLoader {
    Plugin plugin;
    Map<String,Build> map = new HashMap<>();
    public ConfigLoader(Plugin plugin) {
        this.plugin = plugin;
    }

    public ConfigLoader addPath(String path, ConfigBuild build){
        return addPath(path,false,true,build);
    }

    public ConfigLoader addPath(String path, boolean isFolder, ConfigBuild build){
        return addPath(path,isFolder,true,build);
    }

    public ConfigLoader addPath(String path, boolean isFolder, boolean isToYaml, ConfigBuild build){
        Build build1 = new Build(isFolder,isToYaml,build);
        map.put(path,build1);
        return this;
    }

    public void saveConfig(){
        for (Map.Entry<String, Build> kv : map.entrySet()) {
            File file = new File(plugin.getDataFolder(),kv.getKey());
            if (!file.exists()) {
                plugin.saveResource(kv.getKey(), false);
            }
        }
    }

    public void loadConfig(){
        for (Map.Entry<String, Build> kv : map.entrySet()) {
            if (kv.getValue().isFolder){
                File catalogue = new File(plugin.getDataFolder(),kv.getKey());
                for(File file: Objects.requireNonNull(catalogue.listFiles())){
                    if (kv.getValue().isToYaml){
                        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                        kv.getValue().getConfigBuild().getConfig(file,yamlConfiguration);
                    }else{
                        kv.getValue().getConfigBuild().getConfig(file,null);
                    }
                }
            }
            File file = new File(plugin.getDataFolder(),kv.getKey());
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            if (kv.getValue().isToYaml){
                kv.getValue().getConfigBuild().getConfig(file,yamlConfiguration);
            }else{
                kv.getValue().getConfigBuild().getConfig(file,null);
            }
        }
    }

    static class Build{
        boolean isFolder;
        boolean isToYaml;
        ConfigBuild configBuild;

        public Build(boolean isFolder, boolean isToYaml, ConfigBuild configBuild) {
            this.isFolder = isFolder;
            this.isToYaml = isToYaml;
            this.configBuild = configBuild;
        }

        public boolean isFolder() {
            return isFolder;
        }

        public boolean isToYaml() {
            return isToYaml;
        }

        public ConfigBuild getConfigBuild() {
            return configBuild;
        }
    }

    public void desc(){
        System.out.println("必须先addPath之后才能saveConfig");
        System.out.println("这是一个工具类，需new出来");
    }
}
