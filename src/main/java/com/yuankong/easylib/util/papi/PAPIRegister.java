package com.yuankong.easylib.util.papi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PAPIRegister {
    public static void register(String identifier,PAPI papi){
        new PlaceholderExpansion() {
            @Override
            public String getIdentifier() {
                return identifier;
            }

            @Override
            public String getAuthor() {
                return "yuankong";
            }

            @Override
            public String getVersion() {
                return "1.0";
            }

            @Override
            public String onRequest(OfflinePlayer player, String params) {
                StringBuffer s = new StringBuffer();
                papi.map.forEach((str,papiUtil)->{
                    if(str.equals(params)){
                        s.append(papiUtil.setPAPI(player));
                    }
                });
                if(s.length() == 0){
                    if (papi.papiUtils != null){
                        return papi.papiUtils.setPAPI(player,params);
                    }
                    return " ";
                }
                return s.toString();
            }
        }.register();
    }

    public static String getPAPIValue(OfflinePlayer player, String papi){
        return PlaceholderAPI.setPlaceholders(player,papi);
    }
}
