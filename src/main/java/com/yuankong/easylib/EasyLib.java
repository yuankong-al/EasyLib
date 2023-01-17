package com.yuankong.easylib;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.yuankong.easylib.Listener.EventHandler;
import com.yuankong.easylib.api.EasyLibApi;
import com.yuankong.easylib.bungee.Channel;
import com.yuankong.easylib.config.LoadConfig;
import com.yuankong.easylib.event.AfterLoadConfigEvent;
import com.yuankong.easylib.event.ReloadConfigEvent;
import com.yuankong.easylib.event.SQLManagerFinishEvent;
import com.yuankong.easylib.util.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public final class EasyLib extends JavaPlugin {

    public static Plugin instance;
    private static SQLManager sqlManager;
    public static EventHandler eventHandler;
    int flag = 0;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        startMessage();
        LoadConfig.load();
        eventHandler = new EventHandler();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, Channel.REGISTER.getChannel());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, Channel.REGISTER.getChannel(), eventHandler);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, Channel.GENERAL.getChannel());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, Channel.GENERAL.getChannel(), eventHandler);
        if(LoadConfig.isEnable()){
            createManager();
        }else{
            sqlManager = null;
            Bukkit.getConsoleSender().sendMessage("未启用数据库");
        }

        registerChannel();
        registerChannelAgain();
        Bukkit.getScheduler().runTask(this,()->{
            onManager(0);
            AfterLoadConfigEvent event = new AfterLoadConfigEvent(LoadConfig.isEnable(), sqlManager);
            Bukkit.getPluginManager().callEvent(event);
            registerChannel();
        });

        new Timer().start();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){
            command.setUsage("§a/easy reload -重载配置");
        }

        if(!"easy".equals(command.getName())){
            return false;
        }

        if (args.length == 1 && "reload".equals(args[0])) {
            if (sender.isOp()) {
                sender.sendMessage("§f[EasyLib]§a配置重载中，请稍后...");
                LoadConfig.reload();
                boolean endEnable = LoadConfig.isEnable();

                if (endEnable) {
                    sqlManager = null;
                    createManager();
                }

                Bukkit.getScheduler().runTaskLater(EasyLib.instance,()->{
                    onManager(1);
                    sender.sendMessage("§f[EasyLib]§a配置已重载完成！");
                },20);
            }

            return true;
        }
        return !sender.isOp();
    }

    @Override
    public void onDisable() {
        EasySQL.shutdownManager(EasyLib.getSqlManager());
        Bukkit.getConsoleSender().sendMessage("§4========EasyLib已关闭========");
    }

    private void startMessage(){
        Bukkit.getConsoleSender().sendMessage("§a  _____                             __           __        ");
        Bukkit.getConsoleSender().sendMessage("§a /`  __\\                           /` \\       __/` \\       ");
        Bukkit.getConsoleSender().sendMessage("§a \\ \\ \\ __      __      ____  __  __\\ \\ \\     /\\_\\ \\ \\____  ");
        Bukkit.getConsoleSender().sendMessage("§a  \\ \\  __\\   /'__`\\   /',__\\/\\ \\/` \\\\ \\ \\    \\´\\ \\ \\ '__`\\ ");
        Bukkit.getConsoleSender().sendMessage("§a   \\ \\ \\ __ /`\\  \\.\\_/\\__, `\\ \\ \\_\\ \\\\ \\ \\___ \\ \\ \\ \\ \\-\\ \\");
        Bukkit.getConsoleSender().sendMessage("§a    \\ \\____\\\\ \\__/.\\_\\/\\____/\\/`____ \\\\ \\____\\ \\ \\_\\ \\_,__/");
        Bukkit.getConsoleSender().sendMessage("§a     \\/___/  \\/__/\\/_/\\/___/  `/___/> \\\\/____/  \\/_/\\/___/ ");
        Bukkit.getConsoleSender().sendMessage("§a                                 /\\___/                    ");
        Bukkit.getConsoleSender().sendMessage("§a                                 \\/__/                     ");
    }

    private void createManager(){
        sqlManager = EasySQL.createManager(LoadConfig.getDriver(),LoadConfig.getUrl()+LoadConfig.getDatabase()+"?"+LoadConfig.getParameter(),LoadConfig.getUsername(),LoadConfig.getPassword());
        try {
            if(!sqlManager.getConnection().isValid(5)){
                getLogger().warning("数据库连接超时，请reload重试!");
            }else{
                getLogger().info("数据库连接成功!");
            }

        } catch (SQLException e) {
            getLogger().warning("数据库连接失败!!");
        }

    }

    public static SQLManager getSqlManager() {
        return sqlManager;
    }

    private void onManager(int state){
        if(LoadConfig.isEnable() && sqlManager != null){
            if(state == 0){
                Bukkit.getPluginManager().callEvent(new SQLManagerFinishEvent(sqlManager,LoadConfig.isEnable()));
            }else{
                Bukkit.getPluginManager().callEvent(new ReloadConfigEvent(LoadConfig.isEnable(), sqlManager));
            }
            getLogger().info("初始化完成！");
        }else{
            if(LoadConfig.isEnable() && flag != 5){
                getLogger().info("初始化中请稍后...");
                Bukkit.getScheduler().runTaskLater(this, ()->onManager(state),20);
                flag = flag + 1;
            }else{
                if(state == 0){
                    Bukkit.getPluginManager().callEvent(new SQLManagerFinishEvent(null,LoadConfig.isEnable()));
                }else{
                    Bukkit.getPluginManager().callEvent(new ReloadConfigEvent(LoadConfig.isEnable(), sqlManager));
                }
                if(LoadConfig.isEnable()){
                    getLogger().info("初始化失败！");
                }else{
                    getLogger().info("初始化完成！");
                }
            }
        }
    }

    public static void registerChannel(){
        EasyLibApi.channels.forEach(((plugin, list) -> {
            for(String channel:list){
                ByteArrayDataOutput m = ByteStreams.newDataOutput();
                m.writeUTF(channel);
                EasyLib.instance.getServer().sendPluginMessage(EasyLib.instance,Channel.REGISTER.getChannel(), m.toByteArray());
            }
        }));
    }
    public static void registerChannelAgain(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!EventHandler.isRegister){
                    registerChannel();
                }
            }
        }.runTaskLater(EasyLib.instance,5*20);
    }
}
