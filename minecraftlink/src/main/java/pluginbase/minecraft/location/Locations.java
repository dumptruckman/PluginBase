package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class for creating and copying a variety of location designating objects.
 */
public class Locations {

    private Locations() {
        throw new AssertionError();
    }

    public static final FacingCoordinates NULL_FACING = getFacingCoordinates(0D, 0D, 0D, 0F, 0F);

    @NotNull
    public static Coordinates getCoordinates(final double x, final double y, final double z) {
        return new DefaultCoordinates(x, y, z, 0F, 0F);
    }

    @NotNull
    public static FacingCoordinates getFacingCoordinates(@NotNull final Coordinates coords,
                                                         final float pitch, final float yaw) {
        return new DefaultCoordinates(coords.getX(), coords.getY(), coords.getZ(), pitch, yaw);
    }

    @NotNull
    public static FacingCoordinates getFacingCoordinates(final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultCoordinates(x, y, z, pitch, yaw);
    }

    @NotNull
    public static BlockCoordinates getBlockCoordinates(@NotNull final String world,
                                                       final int x, final int y, final int z) {
        return new DefaultWorldCoordinates(world, (double) x, (double) y, (double) z, 0F, 0F);
    }

    @NotNull
    public static BlockCoordinates getBlockCoordinates(@NotNull final String world,
                                                       @NotNull final Coordinates coords) {
        // using the double coordinate getters instead of the getBlock_() methods because it really
        // doesn't matter if we do it now or when somebody calls getBlock_() on the returned object
        return new DefaultWorldCoordinates(world, coords.getX(), coords.getY(), coords.getZ(), 0F, 0F);
    }

    @NotNull
    public static EntityCoordinates getEntityCoordinates(@NotNull final String world,
                                                         final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultWorldCoordinates(world, x, y, z, pitch, yaw);
    }

    @NotNull
    public static EntityCoordinates getEntityCoordinates(@NotNull final BlockCoordinates blockCoords,
                                                         final float pitch, final float yaw) {
        return new DefaultWorldCoordinates(blockCoords.getWorld(), blockCoords.getX(), blockCoords.getY(),
                blockCoords.getZ(), pitch, yaw);
    }

    @NotNull
    public static EntityCoordinates getEntityCoordinates(@NotNull final String world,
                                                         @NotNull final FacingCoordinates facingCoords) {
        return new DefaultWorldCoordinates(world, facingCoords.getX(), facingCoords.getY(), facingCoords.getZ(),
                facingCoords.getPitch(), facingCoords.getYaw());
    }

    @NotNull
    public static MutableCoordinates getMutableCoordinates(final double x, final double y, final double z) {
        return new DefaultMutableCoordinates(x, y, z, 0F, 0F);
    }

    @NotNull
    public static MutableFacingCoordinates getMutableFacingCoordinates(final double x, final double y, final double z,
                                                                       final float pitch, final float yaw) {
        return new DefaultMutableCoordinates(x, y, z, pitch, yaw);
    }

    @NotNull
    public static MutableBlockCoordinates getMutableBlockCoordinates(@NotNull final String world,
                                                                     final int x, final int y, final int z) {
        return new DefaultMutableWorldCoordinates(world, (double) x, (double) y, (double) z, 0F, 0F);
    }

    @NotNull
    public static MutableEntityCoordinates getMutableEntityCoordinates(@NotNull final String world,
                                                                       final double x, final double y, final double z,
                                                                       final float pitch, final float yaw) {
        return new DefaultMutableWorldCoordinates(world, x, y, z, pitch, yaw);
    }

    public static boolean equal(@Nullable final Coordinates a, @Nullable final Coordinates b) {
        return ((a == null) && (b == null)) || ((a != null) && (b != null)
                && (a.getX() == b.getX()) && (a.getY() == b.getY()) && (a.getZ() == b.getZ()));
    }

    public static boolean equal(@Nullable final FacingCoordinates a, @Nullable final FacingCoordinates b) {
        return equal((Coordinates) a, (Coordinates) b)
                && ((a == null) || ((a.getPitch() == b.getPitch()) && (a.getYaw() == b.getYaw())));
    }

    public static boolean equal(@Nullable final EntityCoordinates a, @Nullable final EntityCoordinates b) {
        return equal((FacingCoordinates) a, (FacingCoordinates) b)
                && ((a == null) || ((a.getWorld().equals(b.getWorld()))));
    }
}
