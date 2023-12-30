package com.yuankong.easylib.util.dragon.core;

import com.yuankong.easylib.util.dragon.DragonPacket;
import com.yuankong.easylib.util.dragon.PacketBuilder;
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DragonListener implements Listener {
    public static HashMap<String,PacketManager> managerMap = new HashMap<>();
    @EventHandler
    public void onCustomPacketEvent(CustomPacketEvent event){
        if(event.getData().size() < 1) {
            return;
        }
        PacketManager packetManager = managerMap.get(event.getIdentifier());
        if (packetManager == null){
            return;
        }
        for (Map.Entry<DragonPacket, Method> kv : packetManager.getPacketMap().entrySet()) {
            DragonPacket dragonPacket = kv.getKey();
            Method method = kv.getValue();
            if (!dragonPacket.arg().equals(event.getData().get(0))){
                continue;
            }
            if (dragonPacket.delay() > 0){
                if (System.currentTimeMillis() < packetManager.getDelayTime(event.getPlayer(), dragonPacket)+dragonPacket.delay()){
                    return;
                }
                packetManager.putDelayTime(event.getPlayer(), dragonPacket,System.currentTimeMillis());
            }
            try {
                method.invoke(PacketBuilder.class,event.getPlayer(),event.getData());
            }catch (IllegalAccessException | InvocationTargetException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    public static void register(Plugin plugin,PacketBuilder packetBuilder){
        PacketManager packetManager = new PacketManager(plugin,packetBuilder.packetName());
        if (DragonListener.managerMap.containsKey(packetManager.getPacketName())){
            plugin.getLogger().warning("龙核发包名"+packetManager.getPacketName()+"冲突已被注册，发包注册失败！！！！！");
            return;
        }
        for (Method method : packetBuilder.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(DragonPacket.class)) {
                packetManager.addPacket(method.getAnnotation(DragonPacket.class), method);
            }
        }
        DragonListener.managerMap.put(packetManager.getPacketName(),packetManager);
    }
}
