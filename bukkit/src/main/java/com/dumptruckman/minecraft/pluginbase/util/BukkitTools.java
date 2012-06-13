package com.dumptruckman.minecraft.pluginbase.util;

import org.bukkit.inventory.ItemStack;

public class BukkitTools extends MinecraftTools {

    /**
     * Fills an ItemStack array with air.
     *
     * @param items The ItemStack array to fill.
     * @return The air filled ItemStack array.
     */
    public static ItemStack[] fillWithAir(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            items[i] = new ItemStack(0);
        }
        return items;
    }
}
