package com.dumptruckman.minecraft.pluginbase.entity;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitPlayer extends AbstractBukkitCommandSender<Player> {

    public BukkitPlayer(Player player) {
        super(player);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public void teleport(@NotNull final EntityCoordinates location) {
        final World world = Bukkit.getWorld(location.getWorld());
        if (world == null) {
            Logging.finer("Could not teleport '%s' to target location '%s'.  The target world is not loaded.", getName(), location);
            return;
        }
        final Location l = new Location(world, location.getX(), location.getY(), location.getZ());
        sender.teleport(l);
    }
}
