package com.dumptruckman.minecraft.pluginbase.entity;

import org.bukkit.command.CommandSender;

public class BukkitCommandSender extends BasePlayer {

    private final CommandSender sender;

    public BukkitCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public boolean hasPermission(String perm) {
        return sender.hasPermission(perm);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
