package com.dumptruckman.minecraft.pluginbase.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class Aliases {

    @NotNull
    private final Map<String, String> aliasMap;

    public Aliases(@NotNull final Map<String, String> aliasMap) {
        this.aliasMap = Collections.unmodifiableMap(aliasMap);
    }

    @Nullable
    public String getPropertyForAlias(@NotNull final String alias) {
        return aliasMap.get(alias);
    }
}
