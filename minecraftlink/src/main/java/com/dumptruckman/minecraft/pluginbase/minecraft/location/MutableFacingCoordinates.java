package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a point in 3-dimensional space that also has a pitch and yaw, or facing, value.
 */
public interface MutableFacingCoordinates extends MutableCoordinates, FacingCoordinates {

    /** {@inheritDoc} */
    @Override
    float getPitch();

    /** {@inheritDoc} */
    @Override
    float getYaw();

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates setX(final double x);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates setY(final double y);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates setZ(final double z);

    /**
     * Sets the pitch of the facing.
     * @param pitch the new pitch for the facing.
     * @return this object.
     */
    MutableFacingCoordinates setPitch(final float pitch);

    /**
     * Sets the yaw of the facing.
     * @param yaw the new yaw for the facing.
     * @return this object.
     */
    MutableFacingCoordinates setYaw(final float yaw);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates add(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates subtract(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates multiply(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates divide(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates midpoint(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates multiply(final int m);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates multiply(final double m);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates multiply(final float m);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates crossProduct(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates normalize();

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates add(final double x, final double y, final double z);

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates subtract(final double x, final double y, final double z);
}
