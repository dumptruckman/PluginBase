/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.entity.BukkitCommandSender;
import com.dumptruckman.minecraft.pluginbase.entity.BukkitPlayer;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public class BukkitTools extends MinecraftTools {

    private BukkitTools() {
        throw new AssertionError();
    }

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

    private static final Map<CommandSender, BasePlayer> BASE_PLAYER_MAP = new WeakHashMap<CommandSender, BasePlayer>();

    public static BasePlayer wrapPlayer(@NotNull final Player player) {
        BasePlayer basePlayer = BASE_PLAYER_MAP.get(player);
        if (basePlayer == null) {
            basePlayer = new BukkitPlayer(player);
            BASE_PLAYER_MAP.put(player, basePlayer);
        }
        return basePlayer;
    }

    public static BasePlayer wrapSender(@NotNull final CommandSender sender) {
        if (sender instanceof Player) {
            return wrapPlayer((Player) sender);
        }
        BasePlayer basePlayer = BASE_PLAYER_MAP.get(sender);
        if (basePlayer == null) {
            basePlayer = new BukkitCommandSender(sender);
            BASE_PLAYER_MAP.put(sender, basePlayer);
        }
        return basePlayer;
    }
}
