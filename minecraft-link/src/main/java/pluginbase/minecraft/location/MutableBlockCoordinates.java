package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a point in 3-dimensional space that also has a pitch and yaw, or facing, value.
 */
public interface MutableBlockCoordinates extends MutableCoordinates, BlockCoordinates {

    /**
     * Sets the name of the world for where these coordinates are located.
     *
     * @param world the name of the world for where these coordinates are located.
     * @return this object.
     */
    MutableBlockCoordinates setWorld(@NotNull final String world);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates setX(final double x);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates setY(final double y);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates setZ(final double z);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates add(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates subtract(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates multiply(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates divide(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates midpoint(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates multiply(final int m);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates multiply(final double m);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates multiply(final float m);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates crossProduct(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates normalize();

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates add(final double x, final double y, final double z);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates subtract(final double x, final double y, final double z);

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates mutableCopy();

    /** {@inheritDoc} */
    @Override
    BlockCoordinates immutableCopy();

    /** {@inheritDoc} */
    @Override
    MutableBlockCoordinates getMidpoint(@NotNull final Coordinates o);
}
