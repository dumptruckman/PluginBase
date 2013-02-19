package com.dumptruckman.minecraft.pluginbase.bukkit;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * BasePlayer implementation for a Bukkit CommandSender that is NOT a player.
 */
class BukkitCommandSender extends AbstractBukkitCommandSender<CommandSender> {

    BukkitCommandSender(@NotNull final CommandSender sender) {
        super(sender);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPlayer() {
        return false;
    }
}
