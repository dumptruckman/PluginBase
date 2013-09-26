package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

class DefaultMutableCoordinates extends MutableVector implements MutableFacingCoordinates {

    protected float pitch;
    protected float yaw;

    DefaultMutableCoordinates(final double x, final double y, final double z, final float pitch, final float yaw) {
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

    @Override
    public DefaultMutableCoordinates setX(final double x) {
        return (DefaultMutableCoordinates) super.setX(x);
    }

    @Override
    public DefaultMutableCoordinates setY(final double y) {
        return (DefaultMutableCoordinates) super.setY(y);
    }

    @Override
    public DefaultMutableCoordinates setZ(final double z) {
        return (DefaultMutableCoordinates) super.setZ(z);
    }

    @Override
    public DefaultMutableCoordinates setPitch(final float pitch) {
        this.pitch = pitch;

        return this;
    }

    @Override
    public DefaultMutableCoordinates setYaw(final float yaw) {
        this.yaw = yaw;

        return this;
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
    public DefaultMutableCoordinates getMidpoint(@NotNull final Coordinates o) {
        return new DefaultMutableCoordinates((getX() + o.getX()) / 2, (getY() + o.getY()) / 2, (getZ() + o.getZ()) / 2, getPitch(), getYaw());
    }

    @Override
    public DefaultMutableCoordinates add(@NotNull final Coordinates o) {
        return (DefaultMutableCoordinates) super.add(o);
    }

    @Override
    public DefaultMutableCoordinates subtract(@NotNull final Coordinates o) {
        return (DefaultMutableCoordinates) super.subtract(o);
    }

    @Override
    public DefaultMutableCoordinates multiply(@NotNull final Coordinates o) {
        return (DefaultMutableCoordinates) super.multiply(o);
    }

    @Override
    public DefaultMutableCoordinates divide(@NotNull final Coordinates o) {
        return (DefaultMutableCoordinates) super.divide(o);
    }

    @Override
    public DefaultMutableCoordinates midpoint(@NotNull final Coordinates o) {
        return (DefaultMutableCoordinates) super.midpoint(o);
    }

    @Override
    public DefaultMutableCoordinates multiply(final int m) {
        return (DefaultMutableCoordinates) super.multiply(m);
    }

    @Override
    public DefaultMutableCoordinates multiply(final double m) {
        return (DefaultMutableCoordinates) super.multiply(m);
    }

    @Override
    public DefaultMutableCoordinates multiply(final float m) {
        return (DefaultMutableCoordinates) super.multiply(m);
    }

    @Override
    public DefaultMutableCoordinates add(final double x, final double y, final double z) {
        return (DefaultMutableCoordinates) super.add(x, y, z);
    }

    @Override
    public DefaultMutableCoordinates subtract(final double x, final double y, final double z) {
        return (DefaultMutableCoordinates) super.subtract(x, y, z);
    }

    @Override
    public DefaultMutableCoordinates crossProduct(@NotNull final Coordinates o) {
        return (DefaultMutableCoordinates) super.crossProduct(o);
    }

    @Override
    public DefaultMutableCoordinates normalize() {
        return (DefaultMutableCoordinates) super.normalize();
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

    @Override
    public DefaultMutableCoordinates clone() {
        return (DefaultMutableCoordinates) super.clone();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("DefaultMutableCoordinates {x: %s, y: %s, z: %s, pitch: %s, yaw: %s}", x, y, z, pitch, yaw);
    }
}
