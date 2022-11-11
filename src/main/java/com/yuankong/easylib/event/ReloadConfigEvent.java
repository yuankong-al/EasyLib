package com.yuankong.easylib.event;

import cc.carm.lib.easysql.api.SQLManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 重新加载配置文件事件
 */
public class ReloadConfigEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final boolean enable;
    private final SQLManager sqlManager;

    public ReloadConfigEvent(boolean enable, SQLManager sqlManager) {
        this.enable = enable;
        this.sqlManager = sqlManager;

    }

    public boolean isEnable() {
        return enable;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
