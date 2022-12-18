package com.yuankong.easylib.api;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.util.EasyTool;
import com.yuankong.easylib.util.TimerUtil;
import com.yuankong.easylib.util.UseDate;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EasyLibApi {
    public static SQLManager getSQLManager(){
        return EasyLib.getSqlManager();
    }

    public static HashMap<Plugin, List<TimerUtil>> timerUtils = new HashMap<>();

    @Deprecated
    public static void querySQL(SQLManager sqlManager,EasyTool easyTool, UseDate useDate){
        QueryAction queryAction = sqlManager.createQuery()
                .inTable(easyTool.getTableName())
                .selectColumns(easyTool.getColumnNames())
                .addCondition((String) easyTool.getCondition()[0],easyTool.getCondition()[1])
                .build();
        queryAction.executeAsync(successQuery -> useDate.onResult(successQuery.getResultSet()),(failQuery1, failQuery2) -> useDate.onFail());
    }

    public static void registerTimer(Plugin plugin,TimerUtil timerUtil){
        if(timerUtils.containsKey(plugin)){
            timerUtils.get(plugin).add(timerUtil);
        }else{
            List<TimerUtil> list = new ArrayList<>();
            list.add(timerUtil);
            timerUtils.put(plugin,list);
        }

    }

    public static void unRegisterTimer(Plugin plugin,TimerUtil timerUtil){
        if(timerUtils.containsKey(plugin)){
            timerUtils.get(plugin).remove(timerUtil);
        }
    }

    public static void unRegisterAllTimer(Plugin plugin){
        timerUtils.remove(plugin);
    }
}
