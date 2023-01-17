package com.yuankong.easylib.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BungeeCore extends Plugin implements Listener {
    List<String> channelList = new ArrayList<>();
    @Override
    public void onEnable() {
        this.getProxy().registerChannel(Channel.REGISTER.getChannel());
        this.getProxy().registerChannel(Channel.GENERAL.getChannel());
        this.getProxy().getPluginManager().registerListener(this, this);
        startMessage();
        getProxy().getScheduler().schedule(this, () -> {
            for(ServerInfo server:getProxy().getServers().values()){
                ByteArrayDataOutput m = ByteStreams.newDataOutput();
                m.writeUTF(Channel.START.getChannel());
                server.sendData(Channel.REGISTER.getChannel(),m.toByteArray());
            }
        },5, TimeUnit.SECONDS);
    }
    @Override
    public void onDisable() {
        this.getProxy().unregisterChannel(Channel.REGISTER.getChannel());
        this.getProxy().unregisterChannel(Channel.GENERAL.getChannel());
        for(String channel:channelList){
            this.getProxy().unregisterChannel(channel);
        }
    }

    private void startMessage(){
        System.out.println("§a  _____                             __           __        ");
        System.out.println("§a /`  __\\                           /` \\       __/` \\       ");
        System.out.println("§a \\ \\ \\ __      __      ____  __  __\\ \\ \\     /\\_\\ \\ \\____  ");
        System.out.println("§a  \\ \\  __\\   /'__`\\   /',__\\/\\ \\/` \\\\ \\ \\    \\´\\ \\ \\ '__`\\ ");
        System.out.println("§a   \\ \\ \\ __ /`\\  \\.\\_/\\__, `\\ \\ \\_\\ \\\\ \\ \\___ \\ \\ \\ \\ \\-\\ \\");
        System.out.println("§a    \\ \\____\\\\ \\__/.\\_\\/\\____/\\/`____ \\\\ \\____\\ \\ \\_\\ \\_,__/");
        System.out.println("§a     \\/___/  \\/__/\\/_/\\/___/  `/___/> \\\\/____/  \\/_/\\/___/ ");
        System.out.println("§a                                 /\\___/                    ");
        System.out.println("§a                                 \\/__/                     ");
    }

    @EventHandler
    public void receivePluginMessage(PluginMessageEvent event) {
        //注册插件通道
        if(event.getTag().equalsIgnoreCase(Channel.REGISTER.getChannel())){
            ByteArrayDataInput msg = ByteStreams.newDataInput(event.getData());
            String channel = msg.readUTF();
            if(channelList.contains(channel)){
                return;
            }
            this.getProxy().registerChannel(channel);
            channelList.add(channel);
            this.getLogger().info("["+channel+"]频道注册成功！");
            for(ServerInfo server:getProxy().getServers().values()){
                ByteArrayDataOutput m = ByteStreams.newDataOutput();
                m.writeUTF(Channel.SUCCESS.getChannel());
                server.sendData(Channel.REGISTER.getChannel(),m.toByteArray());
            }
        }
        //通过本插件自带通道传递消息，不会传递给发送消息的那个服务器
        if(event.getTag().equalsIgnoreCase(Channel.GENERAL.getChannel())){
            InetSocketAddress address = event.getSender().getAddress();
            for(ServerInfo server:getProxy().getServers().values()){
                if(!server.getAddress().equals(address)){
                    server.sendData(Channel.GENERAL.getChannel(),event.getData());
                }
            }
        }
        //通过其他插件注册的通道传递消息
        for(String channel:channelList){
            if(event.getTag().equalsIgnoreCase(channel)){
                InetSocketAddress address = event.getSender().getAddress();
                for(ServerInfo server:getProxy().getServers().values()){
                    if(!server.getAddress().equals(address)){
                        server.sendData(channel,event.getData());
                    }
                }
            }
        }
    }
}
