package com.yuankong.easylib.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.api.EasyLibApi;
import com.yuankong.easylib.bungee.Channel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class EventHandler implements PluginMessageListener {
    public static boolean isRegister = false;
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(channel.equals(Channel.REGISTER.getChannel())){
            ByteArrayDataInput msg = ByteStreams.newDataInput(message);
            String str = msg.readUTF();
            if(str.equals(Channel.SUCCESS.getChannel())){
                isRegister = true;
            }
            if(str.equals(Channel.START.getChannel())){
                EasyLib.registerChannel();
                EasyLib.registerChannelAgain();
            }
        }
        if(channel.equals(Channel.GENERAL.getChannel())){
            EasyLibApi.bcMessages.forEach(((plugin, bcMessage) -> {
                try {
                    bcMessage.callBack(channel,message);
                }catch (Exception e){
                    plugin.getLogger().warning(e.getMessage());
                }
            }));
        }

        EasyLibApi.channels.forEach(((plugin, list) -> {
            for(String str:list){
                if(channel.equals(str)){
                    if(EasyLibApi.bcMessages.containsKey(plugin)){
                        EasyLibApi.bcMessages.get(plugin).callBack(channel,message);
                    }
                }
            }
        }));
    }
    /*@org.bukkit.event.EventHandler
    public void onEvent(ServiceUnregisterEvent event) {
        if(LoadConfig.isIsShutdownManagerWhenClose()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(Objects.nonNull(EasyLib.getSqlManager())){
                        EasySQL.shutdownManager(EasyLib.getSqlManager());
                    }
                }
            }.runTaskAsynchronously(EasyLib.instance);
        }

    }*/
}
