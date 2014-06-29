package pluginbase.minecraft;

import org.jetbrains.annotations.NotNull;
import pluginbase.minecraft.location.EntityCoordinates;
import pluginbase.minecraft.location.Vector;

/**
 * Describes an entity in minecraft which is anything that is not a block or an item in an inventory.
 */
public interface Entity {

    /**
     * Gets this {@link Entity}'s {@link EntityCoordinates}.
     *
     * @return this {@link Entity}'s current location
     */
    @NotNull
    EntityCoordinates getLocation();

    /**
     * Teleports this {@link Entity} to the given {@link EntityCoordinates}.
     *
     * @param location new location to teleport this entity to
     * @return true if the teleport was successful
     */
    boolean teleport(@NotNull final EntityCoordinates location);

    /**
     * Gets this {@link Entity}'s current {@link Vector}.
     *
     * @return this {@link Entity}'s current velocity
     */
    Vector getVelocity();

    /**
     * Sets this {@link Entity}'s current {@link Vector}.
     *
     * @param v the new velocity
     */
    void setVelocity(Vector v);
}
