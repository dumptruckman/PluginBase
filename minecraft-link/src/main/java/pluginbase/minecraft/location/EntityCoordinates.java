package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the possible location of an entity in a Minecraft world.
 */
public interface EntityCoordinates extends FacingCoordinates, BlockCoordinates {

    /** {@inheritDoc} */
    @Override
    MutableEntityCoordinates mutableCopy();

    /** {@inheritDoc} */
    @Override
    EntityCoordinates immutableCopy();

    /** {@inheritDoc} */
    @Override
    EntityCoordinates getMidpoint(@NotNull final Coordinates o);
}
