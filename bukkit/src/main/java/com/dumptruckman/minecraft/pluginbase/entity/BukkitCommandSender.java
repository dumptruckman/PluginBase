package com.dumptruckman.minecraft.pluginbase.entity;

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
