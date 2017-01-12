package pluginbase.bukkit.minecraft;

import pluginbase.logging.Logging;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;
import pluginbase.minecraft.location.Vector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * BasePlayer implementation for a Bukkit Player.
 */
class BukkitPlayer extends AbstractBukkitCommandSender<Player> implements Entity {

    BukkitPlayer(@NotNull final Player player) {
        super(player);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPlayer() {
        return true;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public EntityCoordinates getLocation() {
        Location l = getSender().getLocation();
        return Locations.getEntityCoordinates(l.getWorld().getName(), l.getWorld().getUID(),
                l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public boolean teleport(@NotNull final EntityCoordinates location) {
        final World world = Bukkit.getWorld(location.getWorld());
        if (world == null) {
            Logging.warning("Could not teleport '%s' to target location '%s'.  The target world is not loaded.", getName(), location);
            return false;
        }
        final Location l = new Location(world, location.getX(), location.getY(), location.getZ());
        return getSender().teleport(l);
    }

    /** {@inheritDoc} */
    @Override
    public Vector getVelocity() {
        return BukkitTools.convertVector(getSender().getVelocity());
    }

    /** {@inheritDoc} */
    @Override
    public void setVelocity(final Vector v) {
        getSender().setVelocity(BukkitTools.convertVector(v));
    }
}
