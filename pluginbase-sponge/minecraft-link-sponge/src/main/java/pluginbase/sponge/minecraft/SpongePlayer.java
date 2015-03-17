package pluginbase.sponge.minecraft;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Server;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import pluginbase.logging.Logging;
import pluginbase.minecraft.Entity;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Locations;
import pluginbase.minecraft.location.Vector;

// TODO make this class safer... Location stuff is wacky because of how Extents work
public class SpongePlayer extends AbstractSpongeCommandSource<Player> implements Entity {

    public SpongePlayer(@NotNull Player source) {
        super(source);
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
        BlockLoc l = getSender().getLocation().getBlock();
        String worldName = "";
        if (l.getExtent() instanceof World) {
            worldName = ((World) l.getExtent()).getName();
        }
        Vector3f r = getSender().getRotation();
        return Locations.getEntityCoordinates(worldName, l.getX(), l.getY(), l.getZ(), r.getX(), r.getY());
    }

    /** {@inheritDoc} */
    @Override
    public boolean teleport(@NotNull final EntityCoordinates location) {
        final World world = SpongeTools.getServer().getWorld(location.getWorld()).get();
        if (world == null) {
            Logging.warning("Could not teleport '%s' to target location '%s'.  The target world is not loaded.", getName(), location);
            return false;
        }
        final Location l = new Location(world, new Vector3d(location.getX(), location.getY(), location.getZ()));
        return getSender().setLocation(l);
    }

    /** {@inheritDoc} */
    @Override
    public Vector getVelocity() {
        return SpongeTools.convertVector(getSender().getVelocity());
    }

    /** {@inheritDoc} */
    @Override
    public void setVelocity(final Vector v) {
        getSender().setVelocity(SpongeTools.convertVector(v));
    }
}
