package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the possible location of an entity in a Minecraft world.
 */
public interface EntityCoordinates extends FacingCoordinates, BlockCoordinates, Cloneable {

    /**
     * Gets the name of the world in which these coordinates are located.
     *
     * @return The name of the world in which these coordinates are located.
     */
    @NotNull
    public String getWorld();
}
