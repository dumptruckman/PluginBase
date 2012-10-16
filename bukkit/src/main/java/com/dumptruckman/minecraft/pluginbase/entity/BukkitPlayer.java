package com.dumptruckman.minecraft.pluginbase.entity;

import org.bukkit.entity.Player;

public class BukkitPlayer extends AbstractBukkitCommandSender<Player> {

    public BukkitPlayer(Player player) {
        super(player);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
