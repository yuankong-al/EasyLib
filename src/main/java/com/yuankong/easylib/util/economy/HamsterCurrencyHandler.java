package com.yuankong.easylib.util.economy;

import cn.hamster3.currency.api.CurrencyAPI;

import java.util.UUID;

public class HamsterCurrencyHandler {
    public static boolean has(UUID player, String key, double num){
        return CurrencyAPI.hasPlayerCurrency(player,key,num);
    }
    public static double get(UUID player,String key){
        return CurrencyAPI.getPlayerCurrency(player,key);
    }
    public static void take(UUID player, String key, double num){
        CurrencyAPI.takePlayerCurrency(player,key,num);
    }
    public static void set(UUID player, String key, double num){
        CurrencyAPI.setPlayerCurrency(player, key, num);
    }
    public static void add(UUID player, String key, double num){
        CurrencyAPI.addPlayerCurrency(player, key, num);
    }
}
