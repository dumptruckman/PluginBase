package com.dumptruckman.minecraft.pluginbase.minecraft;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Describes an entity in minecraft which is anything that is not a block or an item in an inventory.
 */
public interface Entity {
    /**
     * @return This entity's current location.
     */
    @NotNull
    EntityCoordinates getLocation();

    /**
     * Teleports this entity to the given location.
     *
     * @param location New location to teleport this entity to.
     * @return True if the teleport was successful.
     */
    boolean teleport(@NotNull final EntityCoordinates location);

    /**
     * Gets this {@link Entity}'s current velocity.
     * @return This {@link Entity}'s current velocity.
     */
    Vector getVelocity();

    /**
     * Sets this {@link Entity}'s current velocity.
     * @param v The new velocity.
     */
    void setVelocity(Vector v);
}
