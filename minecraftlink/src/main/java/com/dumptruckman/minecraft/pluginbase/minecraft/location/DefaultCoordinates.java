package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

class DefaultCoordinates extends Vector implements FacingCoordinates {

    protected final float pitch;
    protected final float yaw;

    DefaultCoordinates(final double x, final double y, final double z, final float pitch, final float yaw) {
        super(x, y, z);
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /** {@inheritDoc} */
    @Override
    public float getPitch() {
        return this.pitch;
    }

    /** {@inheritDoc} */
    @Override
    public float getYaw() {
        return this.yaw;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultCoordinates)) return false;

        final DefaultCoordinates other = (DefaultCoordinates) o;
        return Math.abs(x - other.x) < getEpsilon() && Math.abs(y - other.y) < getEpsilon() && Math.abs(z - other.z) < getEpsilon() && Math.abs(pitch - other.pitch) < getEpsilon() && Math.abs(yaw - other.yaw) < getEpsilon() && (this.getClass().equals(o.getClass()));
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
        result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public DefaultCoordinates getMidpoint(@NotNull final Coordinates o) {
        return new DefaultCoordinates((getX() + o.getX()) / 2, (getY() + o.getY()) / 2, (getZ() + o.getZ()) / 2, getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultCoordinates immutableCopy() {
        return new DefaultCoordinates(getX(), getY(), getZ(), getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultMutableCoordinates mutableCopy() {
        return new DefaultMutableCoordinates(getX(), getY(), getZ(), getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultCoordinates clone() {
        return (DefaultCoordinates) super.clone();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("DefaultCoordinates {x: %s, y: %s, z: %s, pitch: %s, yaw: %s}", getX(), getY(), getZ(), getPitch(), getYaw());
    }
}
