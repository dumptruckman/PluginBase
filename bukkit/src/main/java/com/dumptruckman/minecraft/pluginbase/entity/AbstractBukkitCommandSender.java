package com.dumptruckman.minecraft.pluginbase.entity;

import org.bukkit.command.CommandSender;

public abstract class AbstractBukkitCommandSender<S extends CommandSender> extends BasePlayer {

    protected final S sender;

    public AbstractBukkitCommandSender(S sender) {
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
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
}
