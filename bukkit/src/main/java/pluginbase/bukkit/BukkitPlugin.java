/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit;

import pluginbase.command.CommandHandler;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.PluginBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * This interface represents the merger of a PluginBase plugin and a Bukkit plugin.
 * <p/>
 * Contains some convenience overloaded methods and a few methods specific to Bukkit such as wrapper methods.
 */
public interface BukkitPlugin extends PluginBase, Plugin {

    /** {@inheritDoc} */
    @NotNull
    BukkitMessager getMessager();

    /** {@inheritDoc} */
    @NotNull
    CommandHandler getCommandHandler();

    /**
     * Wraps a bukkit Player object into a BasePlayer object for use throughout PluginBase.
     *
     * @param player the Player to wrap.
     * @return the wrapped BasePlayer.
     */
    @NotNull
    BasePlayer wrapPlayer(@NotNull final Player player);

    /**
     * Wraps a bukkit CommandSender object into a BasePlayer object for use throughout PluginBase.
     *
     * @param sender the CommandSender to wrap.
     * @return the wrapped BasePlayer.
     */
    @NotNull
    BasePlayer wrapSender(@NotNull final CommandSender sender);

    /**
     * Gets the name used as a prefix for all permissions for this plugin.
     * <p/>
     * An example would be Multiverse-Core using "multiverse.core" for the return value.
     *
     * @return the name used as a prefix for all permissions for this plugin.
     */
    @NotNull
    String getPermissionName();
}
