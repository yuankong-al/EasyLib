package com.yuankong.easylib.command;

import com.yuankong.easylib.command.tools.CommandUtil;
import org.bukkit.plugin.Plugin;

public interface CommandBuilder {

    String command();
    default String setUsage() {
        return "";
    }
    default void register(Plugin plugin){
        CommandUtil.registerCommand(plugin,command(),setUsage(),this,false);
    }
}
