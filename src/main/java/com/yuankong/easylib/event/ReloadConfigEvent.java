package com.yuankong.easylib.event;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import com.yuankong.easylib.EasyLib;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 重新加载配置文件事件
 */
public class ReloadConfigEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final boolean enable;
    private final SQLManager sqlManager;
    private int num = 0;

    public ReloadConfigEvent(boolean enable, SQLManager sqlManager) {
        this.enable = enable;
        this.sqlManager = sqlManager;
        int i = 0;
        for (SQLManager manager : EasyLib.sqlList) {
            i = i+1;
            if (i == 1){
                continue;
            }
            EasySQL.shutdownManager(manager);
        }
    }

    public boolean isEnable() {
        return enable;
    }

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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
