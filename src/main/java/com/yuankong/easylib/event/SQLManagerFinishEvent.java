package com.yuankong.easylib.event;

import cc.carm.lib.easysql.api.SQLManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 获取到SQLManager后触发的事件，当enable为false时，SQLManager为null
 */
public class SQLManagerFinishEvent  extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final SQLManager sqlManager;
    private final boolean enable;

    public SQLManagerFinishEvent(SQLManager sqlManager, boolean enable) {
        this.sqlManager = sqlManager;
        this.enable = enable;
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
