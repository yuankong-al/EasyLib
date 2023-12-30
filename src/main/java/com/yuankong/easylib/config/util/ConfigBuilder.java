package com.yuankong.easylib.config.util;

import org.bukkit.plugin.Plugin;

public abstract class ConfigBuilder {
    public static ConfigLoader getLoader(Plugin plugin){
        return new ConfigLoader(plugin);
    }
    public abstract void saveConfig();
    public abstract void loadConfig();
}
