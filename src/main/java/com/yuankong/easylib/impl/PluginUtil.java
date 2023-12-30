package com.yuankong.easylib.impl;

import org.bukkit.command.CommandSender;

public interface PluginUtil {
    void onPluginEnable();
    default void onPluginDisable(){}
    default void onReloadCommand(CommandSender sender){}
}
