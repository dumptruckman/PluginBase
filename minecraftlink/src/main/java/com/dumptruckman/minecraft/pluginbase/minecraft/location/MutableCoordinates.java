package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a point in 3-dimensional space.
 * <p/>
 * Unlike {@link Coordinates} this point may be altered.
 */
public interface MutableCoordinates extends Coordinates {

    /**
     * Sets the x coordinate of this point.
     *
     * @param x the new x coordinate of this point.
     * @return this object;
     */
    MutableCoordinates setX(final double x);

    /**
     * Sets the y coordinate of this point.
     *
     * @param y the new y coordinate of this point.
     * @return this object;
     */
    MutableCoordinates setY(final double y);

    /**
     * Sets the z coordinate of this point.
     *
     * @param z the new z coordinate of this point.
     * @return this object;
     */
    MutableCoordinates setZ(final double z);

    /**
     * Adds a point to this one.
     *
     * @param o The other point.
     * @return this object.
     */
    MutableCoordinates add(@NotNull final Coordinates o);

    /**
     * Subtracts a point from this one.
     *
     * @param o The other point.
     * @return this object.
     */
    MutableCoordinates subtract(@NotNull final Coordinates o);

    /**
     * Multiplies this vector by another point.
     *
     * @param o The other point.
     * @return this object.
     */
    MutableCoordinates multiply(@NotNull final Coordinates o);

    /**
     * Divides this vector by another point.
     *
     * @param o The other point.
     * @return this object.
     */
    MutableCoordinates divide(@NotNull final Coordinates o);

    /**
     * Creates a new vector representing the midpoint between this vector and another point.
     *
     * @param o The other vector.
     * @return this object.
     */
    MutableCoordinates midpoint(@NotNull final Coordinates o);

    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     *
     * @param m The factor.
     * @return this object.
     */
    MutableCoordinates multiply(final int m);

    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     *
     * @param m The factor.
     * @return this object.
     */
    MutableCoordinates multiply(final double m);

    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     *
     * @param m The factor.
     * @return this object.
     */
    MutableCoordinates multiply(final float m);

    /**
     * Calculates the cross product of this point with another. The cross
     * product is defined as:
     * <p>
     * x = y1 * z2 - y2 * z1<br/>
     * y = z1 * x2 - z2 * x1<br/>
     * z = x1 * y2 - x2 * y1
     *
     * @param o The other point.
     * @return this object.
     */
    MutableCoordinates crossProduct(@NotNull final Coordinates o);

    /**
     * Converts this point to a unit point (a point with length of 1).
     *
     * @return this object.
     */
    MutableCoordinates normalize();

    /**
     * Adds to the x, y and/or z values of this coordinate set.
     *
     * @param x the amount to add to the x coordinate.
     * @param y the amount to add to the y coordinate.
     * @param z the amount to add to the z coordinate.
     * @return this object.
     */
    MutableCoordinates add(final double x, final double y, final double z);

    /**
     * Subtracts from the x, y and/or z values of this coordinate set.
     *
     * @param x the amount to subtract from the x coordinate.
     * @param y the amount to subtract from the y coordinate.
     * @param z the amount to subtract from the z coordinate.
     * @return this object.
     */
    MutableCoordinates subtract(final double x, final double y, final double z);
}
