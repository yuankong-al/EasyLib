package com.yuankong.easylib.event;

import cc.carm.lib.easysql.api.SQLManager;
import com.yuankong.easylib.EasyLib;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * 获取到SQLManager后触发的事件，当enable为false时，SQLManager为null
 */
public class SQLManagerFinishEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final SQLManager sqlManager;
    private final boolean enable;
    private int num = 0;


    public SQLManagerFinishEvent(SQLManager sqlManager, boolean enable) {
        this.sqlManager = sqlManager;
        this.enable = enable;
        EasyLib.sqlList.add(0,sqlManager);
    }

    public boolean getEnable() {
        return enable;
    }

    /**
     * 数据库管理器，可能为空
     */
    public SQLManager getSqlManager() {
        num = num + 1;
        int x = num/3;
        if (x < EasyLib.sqlList.size()){
            return EasyLib.sqlList.get(x);
        }
        SQLManager sql = EasyLib.createSqlManager();
        EasyLib.sqlList.add(sql);
        return sql;
    }

    public SQLManager getOriginSqlManager() {
        return sqlManager;
    }

    public int getNum() {
        return num;
    }

    public List<SQLManager> getSqlList() {
        return EasyLib.sqlList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
