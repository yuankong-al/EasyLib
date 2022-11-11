package com.yuankong.easylib.event;

import cc.carm.lib.easysql.api.SQLManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 读取配置文件事件，每次启动插件时读取完配置文件10秒之后会触发
 */
public class AfterLoadConfigEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final boolean enable;
    private final SQLManager sqlManager;

    public AfterLoadConfigEvent(boolean enable, SQLManager sqlManager) {
        this.enable = enable;
        this.sqlManager = sqlManager;
    }

    public boolean getEnable() {
        return enable;
    }

    /**
     * 数据库管理器，可能为空
     */
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
