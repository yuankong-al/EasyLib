package com.yuankong.easylib.util.dragon.core;

import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class SendPacket {
    LinkedHashMap<String,String> map;
    Player player;
    public SendPacket(Player player){
         map = new LinkedHashMap<>();
         this.player = player;
    }

    public SendPacket addPacket(String str, Object o){
        map.put(str,String.valueOf(o));
        return this;
    }

    public void send(){
        PacketSender.sendSyncPlaceholder(player,map);
    }
}
