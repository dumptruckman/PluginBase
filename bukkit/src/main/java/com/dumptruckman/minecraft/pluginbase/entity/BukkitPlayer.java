package com.dumptruckman.minecraft.pluginbase.entity;

import org.bukkit.entity.Player;

public class BukkitPlayer extends BasePlayer {

    private final Player player;

    public BukkitPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public boolean hasPermission(String perm) {
        return player.hasPermission(perm);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
