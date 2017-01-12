package pluginbase.sponge.minecraft;

import com.flowpowered.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import pluginbase.logging.Logging;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;
import pluginbase.minecraft.location.Vector;

import java.util.Optional;
import java.util.UUID;

// TODO make this class safer... Location stuff is wacky because of how Extents work
public class SpongePlayer extends AbstractSpongeCommandSource<Player> implements Entity {

    public SpongePlayer(@NotNull Player source) {
        super(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlayer() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public EntityCoordinates getLocation() {
        Location l = getSender().getLocation();
        String worldName = "";
        UUID worldUUID = l.getExtent().getUniqueId();
        if (l.getExtent() instanceof World) {
            worldName = ((World) l.getExtent()).getName();

        }
        Vector3d r = getSender().getRotation();
        return Locations.getEntityCoordinates(worldName, worldUUID, l.getX(), l.getY(), l.getZ(), (float) r.getX(), (float) r.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean teleport(@NotNull final EntityCoordinates location) {
        final World world = SpongeTools.getServer().getWorld(location.getWorld()).get();
        if (world == null) {
            Logging.warning("Could not teleport '%s' to target location '%s'.  The target world is not loaded.", getName(), location);
            return false;
        }
        final Location l = new Location(world, new Vector3d(location.getX(), location.getY(), location.getZ()));
        getSender().setLocation(l);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVelocity() {
        Optional<Vector3d> velocityData = getSender().get(Keys.VELOCITY);
        if (velocityData.isPresent()) {
            return SpongeTools.convertVector(velocityData.get());
        }
        return null; // TODO should we just return zero?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVelocity(final Vector v) {
        getSender().offer(Keys.VELOCITY, SpongeTools.convertVector(v));
    }
}
