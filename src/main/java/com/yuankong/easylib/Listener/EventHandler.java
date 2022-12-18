package com.yuankong.easylib.Listener;

import cc.carm.lib.easysql.EasySQL;
import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.config.LoadConfig;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceUnregisterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class EventHandler implements Listener {
    @org.bukkit.event.EventHandler
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

    }
}
