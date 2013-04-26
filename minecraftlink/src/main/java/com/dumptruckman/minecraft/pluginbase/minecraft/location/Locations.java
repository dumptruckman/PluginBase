package com.dumptruckman.minecraft.pluginbase.minecraft.location;

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
    public static FacingCoordinates getFacingCoordinates(final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultCoordinates(x, y, z, pitch, yaw);
    }

    @NotNull
    public static BlockCoordinates getBlockCoordinates(@NotNull final String world,
                                                       final int x, final int y, final int z) {
        return new DefaultWorldCoordinates(world, getFacingCoordinates((double) x, (double) y, (double) z, 0F, 0F));
    }

    @NotNull
    public static EntityCoordinates getEntityCoordinates(@NotNull final String world,
                                                         final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultWorldCoordinates(world, getFacingCoordinates(x, y, z, pitch, yaw));
    }

    @NotNull
    public static Coordinates getImmutableCoordinates(final double x, final double y, final double z) {
        return new DefaultImmutableCoordinates(x, y, z, 0F, 0F);
    }

    @NotNull
    public static FacingCoordinates getImmutableFacingCoordinates(final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultImmutableCoordinates(x, y, z, pitch, yaw);
    }

    @NotNull
    public static BlockCoordinates getImmutableBlockCoordinates(@NotNull final String world,
                                                       final int x, final int y, final int z) {
        return new DefaultImmutableWorldCoordinates(world, getImmutableFacingCoordinates((double) x, (double) y, (double) z, 0F, 0F));
    }

    @NotNull
    public static EntityCoordinates getImmutableEntityCoordinates(@NotNull final String world,
                                                         final double x, final double y, final double z,
                                                         final float pitch, final float yaw) {
        return new DefaultImmutableWorldCoordinates(world, getImmutableFacingCoordinates(x, y, z, pitch, yaw));
    }

    @NotNull
    public static BlockCoordinates copyOf(@NotNull final BlockCoordinates coords) {
        if (coords instanceof DefaultImmutableWorldCoordinates) {
            return getImmutableBlockCoordinates(coords.getWorld(), coords.getBlockX(), coords.getBlockY(), coords.getBlockZ());
        }
        return getBlockCoordinates(coords.getWorld(), coords.getBlockX(), coords.getBlockY(), coords.getBlockZ());
    }

    @NotNull
     public static EntityCoordinates copyOf(@NotNull final EntityCoordinates coords) {
        if (coords instanceof DefaultImmutableWorldCoordinates) {
            return getImmutableEntityCoordinates(coords.getWorld(), coords.getX(), coords.getY(), coords.getZ(),
                    coords.getPitch(), coords.getYaw());
        }
        return getEntityCoordinates(coords.getWorld(), coords.getX(), coords.getY(), coords.getZ(),
                coords.getPitch(), coords.getYaw());
    }

    @NotNull
    public static FacingCoordinates copyOf(@NotNull final FacingCoordinates coords) {
        if (coords instanceof DefaultImmutableCoordinates) {
            return getImmutableFacingCoordinates(coords.getX(), coords.getY(), coords.getZ(),
                    coords.getPitch(), coords.getYaw());
        }
        return getFacingCoordinates(coords.getX(), coords.getY(), coords.getZ(),
                coords.getPitch(), coords.getYaw());
    }

    @NotNull
    public static Coordinates copyOf(@NotNull final Coordinates coords) {
        if (coords instanceof DefaultImmutableCoordinates) {
            return getImmutableCoordinates(coords.getX(), coords.getY(), coords.getZ());
        }
        return getCoordinates(coords.getX(), coords.getY(), coords.getZ());
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
