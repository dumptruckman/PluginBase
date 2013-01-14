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
}
