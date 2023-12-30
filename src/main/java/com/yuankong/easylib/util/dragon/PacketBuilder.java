package com.yuankong.easylib.util.dragon;

import com.yuankong.easylib.util.dragon.core.DragonListener;
import org.bukkit.plugin.Plugin;

public interface PacketBuilder {
    String packetName();
    default void register(Plugin plugin){
        DragonListener.register(plugin,this);
    }
}
