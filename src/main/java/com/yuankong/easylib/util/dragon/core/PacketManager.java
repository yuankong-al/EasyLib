package com.yuankong.easylib.util.dragon.core;

import com.yuankong.easylib.util.dragon.DragonPacket;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.HashMap;

public class PacketManager {
    String packetName;
    Plugin plugin;
    HashMap<DragonPacket, Method> packetMap = new HashMap<>();
    HashMap<Player,HashMap<DragonPacket,Long>> delayMap = new HashMap<>();

    public PacketManager(Plugin plugin,String packetName) {
        this.packetName = packetName;
        this.plugin = plugin;
    }

    public String getPacketName() {
        return packetName;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public HashMap<DragonPacket, Method> getPacketMap() {
        return packetMap;
    }
    public void addPacket(DragonPacket dragonPacket,Method method){
        this.packetMap.put(dragonPacket,method);
    }

    public long getDelayTime(Player player,DragonPacket dragonPacket){
        Long time = delayMap.get(player).get(dragonPacket);
        if (time == null){
            return 0;
        }
        return time;
    }

    public void putDelayTime(Player player,DragonPacket dragonPacket,long time){
        HashMap<DragonPacket,Long> map = delayMap.get(player);
        if (map == null){
            map = new HashMap<>();
        }
        map.put(dragonPacket,time);
        delayMap.put(player,map);
    }
}
