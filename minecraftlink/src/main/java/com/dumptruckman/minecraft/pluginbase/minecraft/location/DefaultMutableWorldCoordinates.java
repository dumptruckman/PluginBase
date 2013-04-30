package com.dumptruckman.minecraft.pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

class DefaultMutableWorldCoordinates extends DefaultMutableCoordinates implements MutableEntityCoordinates, MutableBlockCoordinates {

    @NotNull
    private String world;

    DefaultMutableWorldCoordinates(@NotNull final String world, final double x, final double y, final double z, final float pitch, final float yaw) {
        super(x, y, z, pitch, yaw);
        this.world = world;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getWorld() {
        return world;
    }

    @Override
    public DefaultMutableWorldCoordinates setWorld(@NotNull final String world) {
        this.world = world;

        return this;
    }

    @Override
    public DefaultMutableWorldCoordinates setX(final double x) {
        return (DefaultMutableWorldCoordinates) super.setX(x);
    }

    @Override
    public DefaultMutableWorldCoordinates setY(final double y) {
        return (DefaultMutableWorldCoordinates) super.setY(y);
    }

    @Override
    public DefaultMutableWorldCoordinates setZ(final double z) {
        return (DefaultMutableWorldCoordinates) super.setZ(z);
    }

    @Override
    public DefaultMutableWorldCoordinates setPitch(final float pitch) {
        return (DefaultMutableWorldCoordinates) super.setPitch(pitch);
    }

    @Override
    public DefaultMutableWorldCoordinates setYaw(final float yaw) {
        return (DefaultMutableWorldCoordinates) super.setYaw(yaw);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DefaultMutableWorldCoordinates that = (DefaultMutableWorldCoordinates) o;
        return super.equals(that) && world.equals(that.world);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return 31 * world.hashCode() + super.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public DefaultWorldCoordinates immutableCopy() {
        return new DefaultWorldCoordinates(getWorld(), getX(), getY(), getZ(), getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultMutableWorldCoordinates mutableCopy() {
        return new DefaultMutableWorldCoordinates(getWorld(), getX(), getY(), getZ(), getPitch(), getYaw());
    }

    /** {@inheritDoc} */
    @Override
    public DefaultMutableWorldCoordinates clone() {
        return (DefaultMutableWorldCoordinates) super.clone();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("DefaultMutableWorldCoordinates {world: %s, x: %s, y: %s, z: %s, pitch: %s, yaw: %s}", getWorld(), getX(), getY(), getZ(), getPitch(), getYaw());
    }

    @Override
    public DefaultMutableWorldCoordinates getMidpoint(@NotNull final Coordinates o) {
        return new DefaultMutableWorldCoordinates(getWorld(), (getX() + o.getX()) / 2, (getY() + o.getY()) / 2, (getZ() + o.getZ()) / 2, getPitch(), getYaw());
    }

    @Override
    public DefaultMutableWorldCoordinates add(@NotNull final Coordinates o) {
        return (DefaultMutableWorldCoordinates) super.add(o);
    }

    @Override
    public DefaultMutableWorldCoordinates subtract(@NotNull final Coordinates o) {
        return (DefaultMutableWorldCoordinates) super.subtract(o);
    }

    @Override
    public DefaultMutableWorldCoordinates multiply(@NotNull final Coordinates o) {
        return (DefaultMutableWorldCoordinates) super.multiply(o);
    }

    @Override
    public DefaultMutableWorldCoordinates divide(@NotNull final Coordinates o) {
        return (DefaultMutableWorldCoordinates) super.divide(o);
    }

    @Override
    public DefaultMutableWorldCoordinates midpoint(@NotNull final Coordinates o) {
        return (DefaultMutableWorldCoordinates) super.midpoint(o);
    }

    @Override
    public DefaultMutableWorldCoordinates multiply(final int m) {
        return (DefaultMutableWorldCoordinates) super.multiply(m);
    }

    @Override
    public DefaultMutableWorldCoordinates multiply(final double m) {
        return (DefaultMutableWorldCoordinates) super.multiply(m);
    }

    @Override
    public DefaultMutableWorldCoordinates multiply(final float m) {
        return (DefaultMutableWorldCoordinates) super.multiply(m);
    }

    @Override
    public DefaultMutableWorldCoordinates add(final double x, final double y, final double z) {
        return (DefaultMutableWorldCoordinates) super.add(x, y, z);
    }

    @Override
    public DefaultMutableWorldCoordinates subtract(final double x, final double y, final double z) {
        return (DefaultMutableWorldCoordinates) super.subtract(x, y, z);
    }

    @Override
    public DefaultMutableWorldCoordinates crossProduct(@NotNull final Coordinates o) {
        return (DefaultMutableWorldCoordinates) super.crossProduct(o);
    }

    @Override
    public DefaultMutableWorldCoordinates normalize() {
        return (DefaultMutableWorldCoordinates) super.normalize();
    }
}
