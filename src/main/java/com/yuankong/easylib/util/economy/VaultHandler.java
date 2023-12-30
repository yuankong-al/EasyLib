package com.yuankong.easylib.util.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class VaultHandler {
    static Economy econ = null;
    public static boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEcon(){
        return econ;
    }
    public static boolean hasMoney(OfflinePlayer player,double num){
        return getEcon().has(player, num);
    }
    public static void withdraw(OfflinePlayer player,double num){
        getEcon().withdrawPlayer(player, num);
    }
    public static double getBalance(OfflinePlayer player){
        return getEcon().getBalance(player);
    }
}
