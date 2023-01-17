package com.yuankong.easylib.util.papi;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class PAPI {
    public final String identifier;
    public HashMap<String,PAPIUtil> map = new HashMap<>();

    public PAPI(String identifier){
        this.identifier = identifier;
    }

    public PAPI setPAPI(String params,PAPIUtil papiUtil){
        map.put(params,papiUtil);
        return this;
    }

    public void build(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return;
        }
        PAPIRegister.register(identifier,this);
    }

}
