package com.dumptruckman.minecraft.pluginbase.bukkit;

import org.bukkit.command.CommandSender;

public class BukkitCommandSender extends AbstractBukkitCommandSender<CommandSender> {

    public BukkitCommandSender(CommandSender sender) {
        super(sender);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
