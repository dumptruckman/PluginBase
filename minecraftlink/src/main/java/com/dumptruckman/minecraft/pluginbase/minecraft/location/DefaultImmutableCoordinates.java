package com.dumptruckman.minecraft.pluginbase.minecraft.location;

class DefaultImmutableCoordinates implements Coordinates, FacingCoordinates {

    private final double x, y, z;
    private final float pitch, yaw;

    DefaultImmutableCoordinates(final double x, final double y, final double z, final float pitch, final float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public float getYaw() {
        return this.yaw;
    }

    @Override
    public void add(final double x, final double y, final double z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void subtract(final double x, final double y, final double z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DefaultImmutableCoordinates that = (DefaultImmutableCoordinates) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0
                && Float.compare(that.yaw, yaw) == 0 && Float.compare(that.pitch, pitch) == 0;
    }

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
}
