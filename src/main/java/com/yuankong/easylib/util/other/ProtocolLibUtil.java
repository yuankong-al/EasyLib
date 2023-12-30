package com.yuankong.easylib.util.other;

import com.comphenix.protocol.utility.StreamSerializer;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ProtocolLibUtil {
    public static String serializeItemStack(ItemStack itemStack) throws IOException {
        return new StreamSerializer().serializeItemStack(itemStack);
    }

    public static ItemStack deserializeItemStack(String str) throws IOException {
        return new StreamSerializer().deserializeItemStack(str);
    }
}
