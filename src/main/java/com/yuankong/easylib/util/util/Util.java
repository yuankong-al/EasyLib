package com.yuankong.easylib.util.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Util {
    public static boolean compareItem(ItemStack hadItem, ItemBase item){
        if(!hadItem.hasItemMeta() || !hadItem.getItemMeta().hasDisplayName()){
            return false;
        }
        if(item.type != null){
            if(item.getType()[0] != hadItem.getType().getId()){
                return false;
            }
            if((byte) item.getType()[1] != hadItem.getData().getData()){
                return false;
            }
        }
        if(!hadItem.hasItemMeta() || !hadItem.getItemMeta().hasDisplayName() || !hadItem.getItemMeta().hasLore()){
            return false;
        }
        if(!hadItem.getItemMeta().getDisplayName().equals(item.getName())){
            return false;
        }
        List<String> itemLore = hadItem.getItemMeta().getLore();
        if(itemLore.isEmpty()){
            return false;
        }
        List<String> list = new ArrayList<>(itemLore);
        list.removeAll(item.getLore());
        return itemLore.size() - list.size() == item.getLore().size();
    }

    public static boolean compareItem(ItemStack hadItem, String name,List<String> lore,int[] type){
        if(!hadItem.hasItemMeta() || !hadItem.getItemMeta().hasDisplayName()){
            return false;
        }
        if(type != null){
            if(type[0] != hadItem.getType().getId()){
                return false;
            }
            if((byte)type[1] != hadItem.getData().getData()){
                return false;
            }
        }
        if(!hadItem.getItemMeta().getDisplayName().equals(name)){
            return false;
        }
        List<String> itemLore = hadItem.getItemMeta().getLore();
        if(itemLore.isEmpty()){
            return false;
        }
        List<String> list = new ArrayList<>(itemLore);
        list.removeAll(lore);
        return itemLore.size() - list.size() == lore.size();
    }

    public static boolean compareItem(ItemStack hadItem, ItemStack compare){
        if(!hadItem.hasItemMeta() || !hadItem.getItemMeta().hasDisplayName()){
            return false;
        }
        if(!hadItem.getType().equals(compare.getType())){
            return false;
        }
        if(hadItem.getData().getData() != compare.getData().getData()){
            return false;
        }
        if(!hadItem.getItemMeta().getDisplayName().equals(compare.getItemMeta().getDisplayName())){
            return false;
        }
        List<String> itemLore = hadItem.getItemMeta().getLore();
        if(itemLore.isEmpty()){
            return false;
        }
        List<String> list = new ArrayList<>(itemLore);
        list.removeAll(compare.getItemMeta().getLore());
        return itemLore.size() - list.size() == compare.getItemMeta().getLore().size();
    }

    //仅后台执行指令,默认替换player变量
    public static void executeCommand(Player player, List<String> commands){
        executeCommand(player.getName(),commands,"","");
    }

    public static void executeCommand(Player player, List<String> commands,String variable,String value){
        executeCommand(player.getName(),commands,variable,value);
    }

    public static void executeCommand(String playerName, List<String> commands,String variable,String value){
        for(String str:commands){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),str.replaceAll("%player%", playerName).replaceAll(variable,value));
        }
    }

    //多种形式执行指令,默认替换player变量
    public static void executeTypeCommand(Player player, List<String> commands){
        for(String str:commands){
            if(!str.contains("[")){
                continue;
            }
            String[] s = str.split("]");
            String command = s[1].replaceAll("%player%", player.getName());
            executeCommands(player,s[0],command);
        }
    }

    public static void executeTypeCommand(Player player, List<String> commands,String variable,String value){
        for(String str:commands){
            if(!str.contains("[")){
                continue;
            }
            String[] s = str.split("]");
            String command = s[1].replaceAll("%player%", player.getName()).replaceAll(variable,value);
            executeCommands(player,s[0],command);
        }
    }

    public static void executeCommands(Player player,String str,String command){
        if(str.toLowerCase().contains("cmd")){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
        }
        if(str.toLowerCase().contains("op")){
            try {
                player.setOp(true);
                Bukkit.dispatchCommand(player,command);
                player.setOp(false);
            }finally {
                player.setOp(false);
            }
        }
        if(str.toLowerCase().contains("chat")){
            if(!command.contains("/")){
                player.chat("/"+command);
            }else{
                player.chat(command);
            }
        }
    }

    //计算背包指定物品的数量总和
    public static int hadItemAmount(Player player,ItemBase itemBase){
        return Arrays.stream(getInvContents(player)).filter((hadItem)->compareItem(hadItem,itemBase)).mapToInt(ItemStack::getAmount).sum();
    }
    public static int hadItemAmount(Player player,ItemStack compare){
        return Arrays.stream(getInvContents(player)).filter((hadItem)->compareItem(hadItem,compare)).mapToInt(ItemStack::getAmount).sum();
    }

    //获取玩家背包除了盔甲槽和副手槽的内容
    public static ItemStack[] getInvContents(Player player){
        Inventory inventory = player.getInventory();
        ItemStack[] itemStacks = new ItemStack[36];
        int i = 0;
        for (ItemStack content : inventory.getContents()) {
            if(i == 36){
                continue;
            }
            itemStacks[i] = content;
            i = i + 1;
        }
        return itemStacks;
    }

    //扣除物品
    public static void takeMaterial(Player player,ItemBase itemBase,int amount){
        Inventory inventory = player.getInventory();
        take(inventory,itemBase,null,amount);
    }

    public static void takeMaterial(Player player,ItemStack compare,int amount){
        Inventory inventory = player.getInventory();
        take(inventory,null,compare,amount);
    }

    private static void take(Inventory inventory,ItemBase itemBase,ItemStack compare,int amount){
        for(ItemStack itemStack:inventory.getContents()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)){
                continue;
            }
            if(itemBase != null && !compareItem(itemStack,itemBase)){
                continue;
            }
            if(compare != null && !compareItem(itemStack,compare)){
                continue;
            }
            if(itemStack.getAmount() <= amount){
                amount = amount - itemStack.getAmount();
                itemStack.setAmount(0);
            }else{
                itemStack.setAmount(itemStack.getAmount()-amount);
                return;
            }
            if(amount == 0){
                return;
            }
        }
    }

    public static String removeColor(String str){
        return str.replaceAll("[&§]+[a-z0-9]", "");
    }

    public static void removeColor(List<String> list){
        list.replaceAll(Util::removeColor);
    }

    public static String replaceColor(String str){
        return str.replaceAll("&", "§");
    }

    public static void replaceColor(List<String> list){
        list.replaceAll(Util::replaceColor);
    }

    public static GsonBuilder gsonBuilder = new GsonBuilder();
    public static Gson gson = gsonBuilder.create();
    public static String objectToJson(Object o){
        return gsonBuilder.create().toJson(o);
    }

    public static List<String> jsonToList(String json){
        return gsonBuilder.create().fromJson(json, new TypeToken<List<String>>(){}.getType());
    }

    public static Map<String,Object> jsonToMap(String json){
        return gsonBuilder.create().fromJson(json, new TypeToken<Map<String,Object>>(){}.getType());
    }

    public static <T> T jsonToMap(String json,TypeToken<T> type){
        return gsonBuilder.create().fromJson(json, type.getType());
    }

    public static <T> T jsonToClass(String json,Class<T> c){
        return gsonBuilder.create().fromJson(json, c);
    }

    public static String listToString(List<String> list){
        StringBuilder string = new StringBuilder();
        for(String str:list){
            string.append(str).append("\n");
        }
        return string.toString();
    }
    public static StringBuilder listToStringBuilder(List<String> list){
        StringBuilder string = new StringBuilder();
        for(String str:list){
            string.append(str).append("\n");
        }
        return string;
    }

    public static <T> T getRandomObject(Map<T,Double> map){
        if(map.isEmpty()){
            return null;
        }
        if (map.size() == 1){
            for (T t : map.keySet()) {
                return t;
            }
        }
        double random = Math.random();
        double p1 = 0;
        double p2 = 0;
        for (Map.Entry<T, Double> kv : map.entrySet()) {
            double chance = kv.getValue();
            p2 = p2 + chance;
            if(p1 <= random && random < p2){
                return kv.getKey();
            }
            p1 = p2;
        }
        List<T> list = new ArrayList<>(map.keySet());
        return list.get((int)(random*list.size()));
    }

    public static <T> T getRandomObject(Collection<T> list,RandomUtil<T> randomUtil){
        if(list.isEmpty()){
            return null;
        }
        if (list.size() == 1){
            for (T t : list) {
                return t;
            }
        }
        double random = Math.random();
        double p1 = 0;
        double p2 = 0;
        for (T t : list) {
            double chance = randomUtil.getChance(t);
            p2 = p2 + chance;
            if(p1 <= random && random < p2){
                return t;
            }
            p1 = p2;
        }
        List<T> list1 = new ArrayList<>(list);
        return list1.get((int)(random*list1.size()));
    }
}
