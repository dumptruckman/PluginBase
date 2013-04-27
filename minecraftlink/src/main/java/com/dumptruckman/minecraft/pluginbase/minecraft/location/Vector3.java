package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A 3-dimensional immutable mathematical vector.
 */
public final class Vector3 {
    /**
     * The null vector.
     */
    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    private final double x;
    private final double y;
    private final double z;

    public Vector3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return The X component.
     */
    public double getX() {
        return x;
    }

    /**
     * @return The Y component.
     */
    public double getY() {
        return y;
    }

    /**
     * @return The Z component.
     */
    public double getZ() {
        return z;
    }

    /**
     * Calculates the length of this vector.
     * @return {@code |this|}
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Creates a unit vector (same direction as this one but length=1).
     * @return {@code this / |this|}
     */
    public Vector3 normalize() {
        return this.divide(this.length());
    }

    /**
     * Adds this vector to another one.
     * @param other The other vector.
     * @return {@code this + other}
     */
    public Vector3 add(@NotNull final Vector3 other) {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Substracts a vector from this one.
     * @param other The other vector.
     * @return {@code this - other}
     */
    public Vector3 substract(@NotNull final Vector3 other) {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Performs a scalar multiplication.
     * @param value The value that should be multiplied with this vector.
     * @return {@code this * value}
     */
    public Vector3 multiply(final double value) {
        return new Vector3(this.x * value, this.y * value, this.z * value);
    }

    /**
     * Performs a scalar division.
     * @param value The value by which this vector should be divided.
     * @return {@code this / value}
     */
    public Vector3 divide(final double value) {
        return new Vector3(this.x / value, this.y / value, this.z / value);
    }

    /**
     * Performs a scalar multiplication of two vectors.
     * @param other The other vector.
     * @return {@code this · other}
     */
    public double multiplyDot(@NotNull final Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Performs a cross multiplication of two vectors.
     * @param other The other vector.
     * @return {@code this x other}
     */
    public Vector3 multiplyCross(@NotNull final Vector3 other) {
        double x = this.y * other.z - this.z * other.y;
        double y = this.z * other.x - this.x * other.z;
        double z = this.x * other.y - this.y * other.x;
        return new Vector3(x, y, z);
    }

    /**
     * Calculates the distance between this and another vector.
     * @param other The other vector.
     * @return {@code |this - other|}
     */
    public double distance(@NotNull final Vector3 other) {
        return this.substract(other).length();
    }

    /**
     * @return A mutable version of this {@link Vector3}.
     */
    public MutableVector3 toMutable() {
        return new MutableVector3(x, y, z);
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Vector3 v = (Vector3) o;
        return Double.compare(v.x, x) == 0 && Double.compare(v.y, y) == 0 && Double.compare(v.z, z) == 0;
    }

    private static final int INT_BITS = 32;
    private static final int HASHCODE_PRIME = 31;
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> INT_BITS));
        temp = Double.doubleToLongBits(y);
        result = HASHCODE_PRIME * result + (int) (temp ^ (temp >>> INT_BITS));
        temp = Double.doubleToLongBits(z);
        result = HASHCODE_PRIME * result + (int) (temp ^ (temp >>> INT_BITS));
        return result;
    }

    @Override
    public String toString() {
        return String.format("Vector (%s|%s|%s)", x, y, z);
    }
}
