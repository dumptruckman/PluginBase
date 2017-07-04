package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a point in 3-dimensional space that also has a pitch and yaw, or facing, value.
 */
public interface FacingCoordinates extends Coordinates {

    /**
     * Gets the pitch of the facing.
     * <br>
     * The pitch is the up/down angle of the facing.
     *
     * @return the pitch of the facing.
     */
    float getPitch();

    /**
     * Gets the yaw of the facing.
     * <br>
     * The yaw is the left/right angle of the facing.
     *
     * @return the yaw of the facing.
     */
    float getYaw();

    /** {@inheritDoc} */
    @Override
    MutableFacingCoordinates mutableCopy();

    /** {@inheritDoc} */
    @Override
    FacingCoordinates immutableCopy();

    /** {@inheritDoc} */
    @Override
    FacingCoordinates getMidpoint(@NotNull final Coordinates o);

    /**
     * Creates a Vector object representing this FacingCoordinates object.
     *
     * @return a Vector object representing this FacingCoordinates object.
     */
    default Vector getDirection() {
        double vecX = 0, vecY = 0, vecZ = 0;

        double rotX = this.getYaw();
        double rotY = this.getPitch();

        vecY = -Math.sin(Math.toRadians(rotY));

        double xz = Math.cos(Math.toRadians(rotY));

        vecX = -xz * Math.sin(Math.toRadians(rotX));
        vecZ = xz * Math.cos(Math.toRadians(rotX));

        return new Vector(vecX, vecY, vecZ);
    }
}
