package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * A 3-dimensional mutable mathematical vector.
 * <p/>
 * Any mutator methods in this class will modify this object.
 */
public class MutableVector extends VectorBase implements MutableCoordinates, Cloneable {

    /**
     * Construct the vector with all components as 0.
     */
    public MutableVector() {
        super();
    }

    /**
     * Construct the vector with provided integer components.
     *
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     */
    public MutableVector(int x, int y, int z) {
        super(x, y, z);
    }

    /**
     * Construct the vector with provided double components.
     *
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     */
    public MutableVector(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Construct the vector with provided float components.
     *
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     */
    public MutableVector(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public MutableCoordinates setX(final double x) {
        this.x = x;

        return this;
    }

    @Override
    public MutableCoordinates setY(final double y) {
        this.x = x;

        return this;
    }

    @Override
    public MutableCoordinates setZ(final double z) {
        this.x = x;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector getMidpoint(@NotNull final Coordinates o) {
        return new MutableVector((getX() + o.getX()) / 2, (getY() + o.getY()) / 2, (getZ() + o.getZ()) / 2);
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector add(@NotNull final Coordinates o) {
        x += o.getX();
        y += o.getY();
        z += o.getZ();

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector subtract(@NotNull final Coordinates o) {
        x -= o.getX();
        y -= o.getY();
        z -= o.getZ();

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector multiply(@NotNull final Coordinates o) {
        x *= o.getX();
        y *= o.getY();
        z *= o.getZ();

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector divide(@NotNull final Coordinates o) {
        x /= o.getX();
        y /= o.getY();
        z /= o.getZ();

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector midpoint(@NotNull final Coordinates o) {
        x = (getX() + o.getX()) / 2;
        y = (getY() + o.getY()) / 2;
        z = (getZ() + o.getZ()) / 2;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector multiply(final int m) {
        x *= m;
        y *= m;
        z *= m;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector multiply(final double m) {
        x *= m;
        y *= m;
        z *= m;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector multiply(final float m) {
        x *= m;
        y *= m;
        z *= m;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector add(final double x, final double y, final double z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector subtract(final double x, final double y, final double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector crossProduct(@NotNull final Coordinates o) {
        double newX = getY() * o.getZ() - o.getY() * getZ();
        double newY = getZ() * o.getX() - o.getZ() * getX();
        double newZ = getX() * o.getY() - o.getX() * getY();

        x = newX;
        y = newY;
        z = newZ;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector normalize() {
        double length = length();
        x /= length;
        y /= length;
        z /= length;

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector clone() {
        try {
            return (MutableVector) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("MutableVector [%s, %s, %s]", getX(), getY(), getZ());
    }

    /**
     * Gets the minimum components of two points.
     *
     * @param v1 The first point.
     * @param v2 The second point.
     * @return a new vector representing the minimum of the two points.
     */
    public static MutableVector getMinimum(@NotNull final Coordinates v1, @NotNull final Coordinates v2) {
        return new MutableVector(Math.min(v1.getX(), v2.getX()), Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));
    }

    /**
     * Gets the maximum components of two points.
     *
     * @param v1 The first point.
     * @param v2 The second point.
     * @return a new vector representing the maximum of the two points.
     */
    public static MutableVector getMaximum(@NotNull final Coordinates v1, @NotNull final Coordinates v2) {
        return new MutableVector(Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()), Math.max(v1.getZ(), v2.getZ()));
    }
}
