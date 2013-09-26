/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit;

import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.location.Vector;
import pluginbase.util.MinecraftTools;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Contains useful utility methods for operations related to Bukkit.
 */
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
    public static ItemStack[] fillWithAir(@NotNull final ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            items[i] = new ItemStack(0);
        }
        return items;
    }

    private static final Map<CommandSender, BasePlayer> BASE_PLAYER_MAP = new WeakHashMap<CommandSender, BasePlayer>();

    /**
     * Wraps a bukkit Player object into a BasePlayer object for use throughout PluginBase.
     *
     * @param player the Player to wrap.
     * @return the wrapped BasePlayer.
     */
    @NotNull
    public static BasePlayer wrapPlayer(@NotNull final Player player) {
        BasePlayer basePlayer = BASE_PLAYER_MAP.get(player);
        if (basePlayer == null) {
            basePlayer = new BukkitPlayer(player);
            BASE_PLAYER_MAP.put(player, basePlayer);
        }
        return basePlayer;
    }

    /**
     * Wraps a bukkit CommandSender object into a BasePlayer object for use throughout PluginBase.
     *
     * @param sender the CommandSender to wrap.
     * @return the wrapped BasePlayer.
     */
    @NotNull
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

    /**
     * Converts {@link org.bukkit.util.Vector} objects to
     * {@link pluginbase.minecraft.location.Vector} objects.
     *
     * @param v The Bukkit vector.
     * @return The PluginBase vector.
     */
    @NotNull
    public static Vector convertVector(@NotNull final org.bukkit.util.Vector v) {
        return new Vector(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Converts {@link pluginbase.minecraft.location.Vector} objects
     * to {@link org.bukkit.util.Vector} objects.
     *
     * @param v The PluginBase vector.
     * @return The Bukkit vector.
     */
    @NotNull
    public static org.bukkit.util.Vector convertVector(final Vector v) {
        return new org.bukkit.util.Vector(v.getX(), v.getY(), v.getZ());
    }
}
