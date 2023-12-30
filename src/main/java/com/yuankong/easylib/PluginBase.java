package com.yuankong.easylib;

import com.yuankong.easylib.command.CmdExecutor;
import com.yuankong.easylib.command.CommandBuilder;
import com.yuankong.easylib.command.tools.CommandUtil;
import com.yuankong.easylib.command.tools.Identify;
import com.yuankong.easylib.impl.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginBase extends JavaPlugin implements PluginUtil, CommandBuilder {
    private boolean reloadCommandUnregister = false;
    @Override
    public void onEnable() {
        commandName = this.getName();
        try{
            saveDefaultConfig();
        }catch (Exception e){
            this.getLogger().warning("插件可能无默认配置文件");
        }
        this.onPluginEnable();
        if (!reloadCommandUnregister){
            CommandUtil.registerCommand(this,command(),setUsage(),this,true);
        }
    }

    @Override
    public void onDisable() {
        this.onPluginDisable();
    }

    private void debug(String debug){
        this.getLogger().info("DEBUG: "+debug);
    }

    public String commandName;
    public void setCommandName(String name){
        commandName = name;
    }

    @Override
    public String command() {
        return commandName;
    }

    public void registerEvent(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener,this);
    }

    @CmdExecutor(arg = "reload",identify = Identify.ALL)
    public void reloadCommand(CommandSender sender,String[] args){
        this.onReloadCommand(sender);
    }

    public void setReloadCommandUnregister(){
        reloadCommandUnregister = true;
    }

    public void desc(){
        System.out.println("只需重写onPluginBase和onReloadCommand");
        System.out.println("修改指令名使用setCommandName");
    }

}
