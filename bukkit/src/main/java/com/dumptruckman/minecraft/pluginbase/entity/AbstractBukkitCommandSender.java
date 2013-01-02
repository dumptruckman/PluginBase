package com.dumptruckman.minecraft.pluginbase.entity;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public void teleport(@NotNull final EntityCoordinates location) {
        Logging.finer("Cannot teleport %s", sender);
    }
}
