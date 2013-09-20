package com.dumptruckman.minecraft.pluginbase.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Properties {

    @Nullable
    Object getProperty(@NotNull String... name) throws NoSuchFieldException, IllegalArgumentException;

    void setProperty(@Nullable Object value, @NotNull String... name) throws NoSuchFieldException, IllegalArgumentException;

    @Nullable
    Object getPropertyUnsafe(@NotNull String... name) throws IllegalArgumentException;

    void setPropertyUnsafe(@Nullable Object value, @NotNull String... name) throws IllegalArgumentException;
}
