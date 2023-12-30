package com.yuankong.easylib.config.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface ConfigBuild {
    void getConfig(File file, YamlConfiguration yamlConfig);
}
