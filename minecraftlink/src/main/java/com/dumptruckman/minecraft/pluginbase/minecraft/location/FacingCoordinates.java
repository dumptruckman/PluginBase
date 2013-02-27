package com.dumptruckman.minecraft.pluginbase.minecraft.location;

/**
 * Represents a point in 3-dimensional space that also has a pitch and yaw, or facing, value.
 */
public interface FacingCoordinates extends Coordinates {

    /**
     * Gets the pitch of the facing.
     * <p/>
     * The pitch is the up/down angle of the facing.
     *
     * @return the pitch of the facing.
     */
    float getPitch();

    /**
     * Gets the yaw of the facing.
     * <p/>
     * The yaw is the left/right angle of the facing.
     *
     * @return the yaw of the facing.
     */
    float getYaw();
}
