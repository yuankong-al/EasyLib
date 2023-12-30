package com.yuankong.easylib.command.tools;

import com.yuankong.easylib.command.CmdExecutor;
import com.yuankong.easylib.command.CommandBuilder;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdManager {
    String command;
    String usage;
    Plugin plugin;
    HashMap<CmdExecutor, Method> executorMap = new HashMap<>();
    List<String> tabSelect = new ArrayList<>();
    CommandBuilder commandBuilder;

    public CmdManager(Plugin plugin,String command, String usage,CommandBuilder commandBuilder) {
        this.command = command;
        this.usage = usage;
        this.plugin = plugin;
        this.commandBuilder = commandBuilder;
    }

    public String getCommand() {
        return command;
    }

    public String getUsage() {
        return usage;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void addExecutor(CmdExecutor executor,Method method){
        executorMap.put(executor,method);
        if (executor.arg().length >= 1){
            tabSelect.add(executor.arg()[0]);
        }
    }

    public List<String> getTabSelect() {
        return tabSelect;
    }

    public HashMap<CmdExecutor, Method> getExecutorMap() {
        return executorMap;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
    public void addUsage(String usage){
        this.usage = this.usage+"\n"+usage;
    }

    public CommandBuilder getCommandBuilder() {
        return commandBuilder;
    }
    public void setCommandBuilder(CommandBuilder commandBuilder){
        this.commandBuilder = commandBuilder;
    }
    public static CmdManager forPluginName(String name){
        for (CmdManager cmdManager : CommandUtil.cmds.values()) {
            if(cmdManager.getPlugin().getName().toLowerCase().equals(name)){
                return cmdManager;
            }
        }
        return null;
    }
}
