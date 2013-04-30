package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the possible location of block in a Minecraft World.
 */
public interface BlockCoordinates extends Coordinates {

    /**
     * Gets the name of the world in which these coordinates are located.
     *
     * @return The name of the world in which these coordinates are located.
     */
    @NotNull
    String getWorld();

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates mutableCopy();

    /** {@inheritDoc} */
    @Override
    BlockCoordinates immutableCopy();

    /** {@inheritDoc} */
    @Override
    BlockCoordinates getMidpoint(@NotNull final Coordinates o);
}
