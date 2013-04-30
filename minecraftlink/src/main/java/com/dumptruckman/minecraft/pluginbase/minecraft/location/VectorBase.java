package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class VectorBase implements Coordinates {

    protected static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    protected double x;
    protected double y;
    protected double z;

    /**
     * Construct the vector with all components as 0.
     */
    protected VectorBase() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Construct the vector with provided integer components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    protected VectorBase(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct the vector with provided double components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    protected VectorBase(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct the vector with provided float components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    protected VectorBase(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** {@inheritDoc} */
    @Override
    public double getX() {
        return this.x;
    }

    /**
     * Gets the floored value of the X component, indicating the block that
     * this vector is contained with.
     *
     * @return block x.
     */
    public int getBlockX() {
        return floor(x);
    }

    /** {@inheritDoc} */
    @Override
    public double getY() {
        return this.y;
    }

    /**
     * Gets the floored value of the Y component, indicating the block that
     * this vector is contained with.
     *
     * @return block y.
     */
    public int getBlockY() {
        return floor(y);
    }

    /** {@inheritDoc} */
    @Override
    public double getZ() {
        return this.z;
    }

    /**
     * Gets the floored value of the Z component, indicating the block that
     * this vector is contained with.
     *
     * @return block z.
     */
    public int getBlockZ() {
        return floor(z);
    }

    /** {@inheritDoc} */
    @Override
    public double length() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2));
    }

    /** {@inheritDoc} */
    @Override
    public double lengthSquared() {
        return Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2);
    }

    /** {@inheritDoc} */
    @Override
    public double distance(@NotNull final Coordinates o) {
        return Math.sqrt(Math.pow(getX() - o.getX(), 2) + Math.pow(getY() - o.getY(), 2) + Math.pow(getZ() - o.getZ(), 2));
    }

    /** {@inheritDoc} */
    @Override
    public double distanceSquared(@NotNull final Coordinates o) {
        return Math.pow(getX() - o.getX(), 2) + Math.pow(getY() - o.getY(), 2) + Math.pow(getZ() - o.getZ(), 2);
    }

    /** {@inheritDoc} */
    @Override
    public float angle(@NotNull final Coordinates o) {
        double dot = dot(o) / (length() * o.length());

        return (float) Math.acos(dot);
    }

    /** {@inheritDoc} */
    @Override
    public double dot(@NotNull final Coordinates o) {
        return getX() * o.getX() + getY() * o.getY() + getZ() * o.getZ();
    }

    /** {@inheritDoc} */
    @Override
    public abstract VectorBase getMidpoint(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    public boolean isInAABB(@NotNull final Coordinates min, @NotNull final Coordinates max) {
        return getX() >= min.getX() && getX() <= max.getX()
                && getY() >= min.getY() && getY() <= max.getY()
                && getZ() >= min.getZ() && getZ() <= max.getZ();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isInSphere(@NotNull final Coordinates origin, final double radius) {
        return (Math.pow(origin.getX() - getX(), 2) + Math.pow(origin.getY() - getY(), 2) + Math.pow(origin.getZ() - getZ(), 2)) <= Math.pow(radius, 2);
    }

    /**
     * Threshold for fuzzy equals().
     */
    private static final double EPSILON = 0.000001D;

    public double getEpsilon() {
        return EPSILON;
    }

    /**
     * Checks to see if two objects are equal.
     * <p>
     * Only two Vectors can ever return true. This method uses a fuzzy match
     * to account for floating point errors. The epsilon can be retrieved
     * with epsilon.
     */
    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (!(o instanceof VectorBase)) return false;

        final VectorBase other = (VectorBase) o;
        return Math.abs(x - other.x) < EPSILON && Math.abs(y - other.y) < EPSILON && Math.abs(z - other.z) < EPSILON && (this.getClass().equals(o.getClass()));
    }

    private static final int INT_BITS = 32;
    private static final int HASHCODE_PRIME = 79;

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = 7;

        hash = HASHCODE_PRIME * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> INT_BITS));
        hash = HASHCODE_PRIME * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> INT_BITS));
        hash = HASHCODE_PRIME * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> INT_BITS));
        return hash;
    }

    /** {@inheritDoc} */
    @Override
    public Vector immutableCopy() {
        return new Vector(getX(), getY(), getZ());
    }

    /** {@inheritDoc} */
    @Override
    public MutableVector mutableCopy() {
        return new MutableVector(getX(), getY(), getZ());
    }

    /** {@inheritDoc} */
    @Override
    public final Vector toImmutableVector() {
        return new Vector(getX(), getY(), getZ());
    }

    /** {@inheritDoc} */
    @Override
    public final MutableVector toMutableVector() {
        return new MutableVector(getX(), getY(), getZ());
    }
}
