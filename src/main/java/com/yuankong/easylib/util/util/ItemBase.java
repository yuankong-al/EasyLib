package com.yuankong.easylib.util.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class ItemBase {
    public String name;
    public String type = null;
    public List<String> lore;

    public String getName() {
        return name;
    }

    public int[] getType() {
        String[] str = type.split(":");
        return new int[]{Integer.parseInt(str[0]),str.length > 1?Integer.parseInt(str[1]):0};
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack getItem(int amount){
        ItemStack itemStack =  new ItemStack(getType()[0],amount, (short) 0, (byte) getType()[1]);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
