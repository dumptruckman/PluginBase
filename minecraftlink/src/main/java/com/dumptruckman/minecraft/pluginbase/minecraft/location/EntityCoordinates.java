package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the possible location of an entity in a Minecraft world.
 */
public interface EntityCoordinates extends FacingCoordinates {

    /**
     * Gets the name of the world in which these coordinates are located.
     *
     * @return The name of the world in which these coordinates are located.
     */
    @NotNull
    String getWorld();

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates mutableCopy();

    /** {@inheritDoc} */
    @Override
    EntityCoordinates immutableCopy();

    /** {@inheritDoc} */
    @Override
    EntityCoordinates getMidpoint(@NotNull final Coordinates o);
}
