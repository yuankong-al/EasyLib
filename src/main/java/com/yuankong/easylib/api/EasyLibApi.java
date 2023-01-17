package com.yuankong.easylib.api;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.bungee.BCMessage;
import com.yuankong.easylib.bungee.Channel;
import com.yuankong.easylib.util.datebase.EasyTool;
import com.yuankong.easylib.util.timer.TimerUtil;
import com.yuankong.easylib.util.datebase.UseDate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EasyLibApi {
    public static SQLManager getSQLManager(){
        return EasyLib.getSqlManager();
    }
    public static HashMap<Plugin, List<TimerUtil>> timerUtils = new HashMap<>();
    public static HashMap<Plugin,List<String>> channels = new HashMap<>();
    public static HashMap<Plugin, BCMessage> bcMessages = new HashMap<>();

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
    public static void registerTimer(Plugin plugin,List<TimerUtil> timerUtilList){
        timerUtils.put(plugin,timerUtilList);
    }

    public static void unRegisterTimer(Plugin plugin,TimerUtil timerUtil){
        if(timerUtils.containsKey(plugin)){
            timerUtils.get(plugin).remove(timerUtil);
        }
    }

    public static void unRegisterAllTimer(Plugin plugin){
        timerUtils.remove(plugin);
    }

    public static void registerBCChannel(Plugin plugin,List<String> list){
        channels.put(plugin,list);
        for(String str:list){
            if(!EasyLib.instance.getServer().getMessenger().isIncomingChannelRegistered(EasyLib.instance,str)){
                EasyLib.instance.getServer().getMessenger().registerOutgoingPluginChannel(EasyLib.instance, str);
                EasyLib.instance.getServer().getMessenger().registerIncomingPluginChannel(EasyLib.instance, str, EasyLib.eventHandler);
            }
        }

    }

    public static void registerBCMessage(Plugin plugin,BCMessage bcMessage){
        bcMessages.put(plugin,bcMessage);
    }
    public static void unRegisterBCMessage(Plugin plugin){
        bcMessages.remove(plugin);
    }
    public static void unRegisterBCMessage(BCMessage bcMessage){
        for(Plugin plugin:bcMessages.keySet()){
            if(bcMessages.get(plugin).equals(bcMessage)){
                bcMessages.remove(plugin);
                return;
            }
        }
    }

    public static void sendBCPacket(String channel,List<String> packet){
        ByteArrayDataOutput m = ByteStreams.newDataOutput();
        for(String str:packet){
            m.writeUTF(str);
        }
        Bukkit.getServer().sendPluginMessage(EasyLib.instance,channel,m.toByteArray());
    }

}
