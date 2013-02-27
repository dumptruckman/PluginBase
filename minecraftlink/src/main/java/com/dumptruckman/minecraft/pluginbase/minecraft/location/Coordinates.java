package com.dumptruckman.minecraft.pluginbase.minecraft.location;

/**
 * Represents a point in 3-dimensional space.
 */
public interface Coordinates {

    /**
     * Gets the x coordinate of this point.
     *
     * @return The x coordinate of this point.
     */
    double getX();

    /**
     * Gets the y coordinate of this point.
     *
     * @return The y coordinate of this point.
     */
    double getY();

    /**
     * Gets the z coordinate of this point.
     *
     * @return The z coordinate of this point.
     */
    double getZ();

    /**
     * Adds to the x, y and/or z values of this coordinate set.
     *
     * @param x the amount to add to the x coordinate.
     * @param y the amount to add to the y coordinate.
     * @param z the amount to add to the z coordinate.
     */
    void add(final double x, final double y, final double z);

    /**
     * Subtracts from the x, y and/or z values of this coordinate set.
     *
     * @param x the amount to subtract from the x coordinate.
     * @param y the amount to subtract from the y coordinate.
     * @param z the amount to subtract from the z coordinate.
     */
    void subtract(final double x, final double y, final double z);
}
