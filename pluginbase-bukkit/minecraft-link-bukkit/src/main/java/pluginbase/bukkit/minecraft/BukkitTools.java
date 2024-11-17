/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.location.BlockCoordinates;
import pluginbase.minecraft.location.Coordinates;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;
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
            items[i] = new ItemStack(Material.AIR);
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
    public static org.bukkit.util.Vector convertVector(@NotNull final Vector v) {
        return new org.bukkit.util.Vector(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Converts a PluginBase coordinates object into a Bukkit Location.
     * <p/>
     * Be aware that if the world the coordinates refers to is not loaded the location will contain a null world
     * reference.
     *
     * @param c The PluginBase coordinates.
     * @return The Bukkit Location.
     */
    @NotNull
    public static Location coordinatesToLocation(@NotNull final BlockCoordinates c) {
        World world = Bukkit.getWorld(c.getWorldUUID());
        return new Location(world, c.getX(), c.getY(), c.getZ());
    }

    /**
     * Converts a Bukkit Location to a PluginBase coordinates object.
     *
     * @param l The Bukkit location.
     * @return The PluginBase coordinates.
     * @throws IllegalArgumentException Thrown if the location has a null world.
     */
    @NotNull
    public static EntityCoordinates locationToCoordinates(@NotNull final Location l)
            throws IllegalArgumentException {
        World w = l.getWorld();
        if (w == null) {
            throw new IllegalArgumentException("World cannot be null in location to convert");
        }
        return Locations.getEntityCoordinates(w.getName(), w.getUID(), l.getX(), l.getY(), l.getZ(),
                l.getPitch(), l.getYaw());
    }

    /**
     * Converts a PluginBase coordinates object into a Bukkit Block.
     *
     * @param c The PluginBase coordinates.
     * @return The Bukkit block.
     * @throws IllegalArgumentException Thrown if the coordinates refers to a world that is not loaded.
     */
    @NotNull
    public static Block coordinatesToBlock(@NotNull final BlockCoordinates c)
            throws IllegalArgumentException {
        World world = Bukkit.getWorld(c.getWorldUUID());
        if (world == null) {
            throw new IllegalArgumentException("World referenced by coordinates is not loaded");
        }
        return world.getBlockAt(c.getBlockX(), c.getBlockY(), c.getBlockZ());
    }

    /**
     * Converts a Bukkit Block to a PluginBase coordinates object.
     *
     * @param b The Bukkit block.
     * @return The PluginBase coordinates.
     */
    @NotNull
    public static BlockCoordinates blockToCoordinates(@NotNull final Block b) {
        World w = b.getWorld();
        return Locations.getBlockCoordinates(w.getName(), w.getUID(), b.getX(), b.getY(), b.getZ());
    }
}
