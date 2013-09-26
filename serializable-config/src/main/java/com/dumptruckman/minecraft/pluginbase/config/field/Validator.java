package com.dumptruckman.minecraft.pluginbase.config.field;

import org.jetbrains.annotations.Nullable;

import java.beans.PropertyVetoException;

public interface Validator {

    @Nullable
    Object validateChange(@Nullable Object newValue, @Nullable Object oldValue) throws PropertyVetoException;
}
