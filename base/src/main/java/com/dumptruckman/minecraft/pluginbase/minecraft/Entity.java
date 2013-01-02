package com.dumptruckman.minecraft.pluginbase.minecraft;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.EntityCoordinates;
import org.jetbrains.annotations.NotNull;

public interface Entity {

    void teleport(@NotNull final EntityCoordinates location);
}
