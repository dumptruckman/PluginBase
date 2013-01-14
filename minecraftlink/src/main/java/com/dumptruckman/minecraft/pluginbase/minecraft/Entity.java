package com.dumptruckman.minecraft.pluginbase.minecraft;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import org.jetbrains.annotations.NotNull;

public interface Entity {

    /**
     * Teleports this entity to the given location.
     *
     * @param location New location to teleport this entity to.
     * @return True if the teleport was successful.
     */
    boolean teleport(@NotNull final EntityCoordinates location);
}
