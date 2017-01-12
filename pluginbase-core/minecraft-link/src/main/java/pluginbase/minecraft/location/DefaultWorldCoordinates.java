package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

class DefaultWorldCoordinates extends DefaultCoordinates implements EntityCoordinates, BlockCoordinates {

    @NotNull
    @Deprecated
    private final String world;
    @NotNull
    private final UUID worldUUID;

    private final int blockX;
    private final int blockY;
    private final int blockZ;

    DefaultWorldCoordinates(@NotNull final String world, @NotNull UUID worldUUID, final double x, final double y, final double z, final float pitch, final float yaw) {
        super(x, y, z, pitch, yaw);
        this.world = world;
        this.worldUUID = worldUUID;
        this.blockX = super.getBlockX();
        this.blockY = super.getBlockY();
        this.blockZ = super.getBlockZ();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getWorld() {
        return world;
    }

    @NotNull
    @Override
    public UUID getWorldUUID() {
        return worldUUID;
    }

    /** {@inheritDoc} */
    @Override
    public int getBlockX() {
        return blockX;
    }

    /** {@inheritDoc} */
    @Override
    public int getBlockY() {
        return blockY;
    }

    /** {@inheritDoc} */
    @Override
    public int getBlockZ() {
        return blockZ;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultWorldCoordinates)) return false;

        final DefaultWorldCoordinates that = (DefaultWorldCoordinates) o;
        return super.equals(that) && world.equals(that.world);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return 31 * world.hashCode() + super.hashCode();
    }

    @Override
    public DefaultWorldCoordinates getMidpoint(@NotNull final Coordinates o) {
        return new DefaultWorldCoordinates(getWorld(), getWorldUUID(), (getX() + o.getX()) / 2, (getY() + o.getY()) / 2, (getZ() + o.getZ()) / 2, getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultWorldCoordinates immutableCopy() {
        return new DefaultWorldCoordinates(getWorld(), getWorldUUID(), getX(), getY(), getZ(), getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultMutableWorldCoordinates mutableCopy() {
        return new DefaultMutableWorldCoordinates(getWorld(), getWorldUUID(), getX(), getY(), getZ(), getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultWorldCoordinates clone() {
        return (DefaultWorldCoordinates) super.clone();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("DefaultWorldCoordinates {world: %s, x: %s, y: %s, z: %s, pitch: %s, yaw: %s}", getWorld(), getX(), getY(), getZ(), getPitch(), getYaw());
    }
}
