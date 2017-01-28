package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A 3-dimensional immutable mathematical vector.
 */
public final class MutableVector3 {
    /**
     * The null vector.
     */
    public static final MutableVector3 ZERO = new MutableVector3(0, 0, 0);

    private double x;
    private double y;
    private double z;

    public MutableVector3(final double x, final double y, final double z) {
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
     * Sets the X component.
     * @param x The new value.
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * Sets the Y component.
     * @param y The new value.
     */
    public void setY(final double y) {
        this.y = y;
    }

    /**
     * Sets the Z component.
     * @param z The new value.
     */
    public void setZ(final double z) {
        this.z = z;
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
     * @return {@code this = this / |this|}
     */
    public MutableVector3 normalize() {
        this.divide(this.length());
        return this;
    }

    /**
     * Adds this vector to another one.
     * @param other The other vector.
     * @return {@code this = this + other}
     */
    public MutableVector3 add(@NotNull final MutableVector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    /**
     * Substracts a vector from this one.
     * @param other The other vector.
     * @return {@code this = this - other}
     */
    public MutableVector3 substract(@NotNull final MutableVector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    /**
     * Performs a scalar multiplication.
     * @param value The value that should be multiplied with this vector.
     * @return {@code this = this * value}
     */
    public MutableVector3 multiply(final double value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }

    /**
     * Performs a scalar division.
     * @param value The value by which this vector should be divided.
     * @return {@code this = this / value}
     */
    public MutableVector3 divide(final double value) {
        this.x /= value;
        this.y /= value;
        this.z /= value;
        return this;
    }

    /**
     * Performs a scalar multiplication of two vectors.
     * @param other The other vector.
     * @return {@code this · other}
     */
    public double multiplyDot(@NotNull final MutableVector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Performs a cross multiplication of two vectors.
     * @param other The other vector.
     * @return {@code this = this x other}
     */
    public MutableVector3 multiplyCross(@NotNull final MutableVector3 other) {
        double x = this.y * other.z - this.z * other.y;
        double y = this.z * other.x - this.x * other.z;
        double z = this.x * other.y - this.y * other.x;

        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    /**
     * Calculates the distance between this and another vector.
     * @param other The other vector.
     * @return {@code |this - other|}
     */
    public double distance(@NotNull final MutableVector3 other) {
        return this.substract(other).length();
    }

    /**
     * @return An immutable version of this {@link MutableVector3}.
     */
    public Vector3 toImmutable() {
        return new Vector3(x, y, z);
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MutableVector3 v = (MutableVector3) o;
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
