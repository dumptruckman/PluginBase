package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the possible location of block in a Minecraft World.
 */
public interface BlockCoordinates {

    /**
     * Gets the name of the world in which these coordinates are located.
     *
     * @return The name of the world in which these coordinates are located.
     */
    @NotNull
    String getWorld();

    /**
     * Gets the block x coordinate represented by this location.
     *
     * @return The block x coordinate represented by this location.
     */
    int getBlockX();

    /**
     * Gets the block y coordinate represented by this location.
     *
     * @return The block y coordinate represented by this location.
     */
    int getBlockY();

    /**
     * Gets the block z coordinate represented by this location.
     *
     * @return The block z coordinate represented by this location.
     */
    int getBlockZ();

    /**
     * Adds to the x, y and/or z values of this coordinate set.
     *
     * @param x the amount to add to the x coordinate.
     * @param y the amount to add to the y coordinate.
     * @param z the amount to add to the z coordinate.
     */
    void add(final int x, final int y, final int z);

    /**
     * Subtracts from the x, y and/or z values of this coordinate set.
     *
     * @param x the amount to subtract from the x coordinate.
     * @param y the amount to subtract from the y coordinate.
     * @param z the amount to subtract from the z coordinate.
     */
    void subtract(final int x, final int y, final int z);
}
