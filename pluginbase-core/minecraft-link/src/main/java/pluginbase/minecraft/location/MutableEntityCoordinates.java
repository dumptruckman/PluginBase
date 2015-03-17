package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a point in 3-dimensional space that also has a pitch and yaw, or facing, value.
 */
public interface MutableEntityCoordinates extends EntityCoordinates, MutableFacingCoordinates, MutableBlockCoordinates {

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates setWorld(@NotNull final String world);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates setPitch(final float pitch);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates setYaw(final float yaw);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates setX(final double x);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates setY(final double y);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates setZ(final double z);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates add(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates subtract(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates multiply(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates divide(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates midpoint(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates multiply(final int m);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates multiply(final double m);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates multiply(final float m);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates crossProduct(@NotNull final Coordinates o);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates normalize();

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates add(final double x, final double y, final double z);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates subtract(final double x, final double y, final double z);

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates mutableCopy();

    /** {@inheritDoc} */
    @Override
    EntityCoordinates immutableCopy();

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates getMidpoint(@NotNull final Coordinates o);
}
