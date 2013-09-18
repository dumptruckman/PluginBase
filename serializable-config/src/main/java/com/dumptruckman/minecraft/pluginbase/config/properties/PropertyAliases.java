package com.dumptruckman.minecraft.pluginbase.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PropertyAliases {

    @NotNull
    private static final Map<Class, PropertyAliases> classAliasMap = new HashMap<Class, PropertyAliases>();

    private final Map<String, String[]> aliasMap = new HashMap<String, String[]>();

    private static PropertyAliases getAliases(@NotNull Class clazz) {
        return classAliasMap.get(clazz);
    }

    private static PropertyAliases createAliases(@NotNull Class clazz) {
        PropertyAliases aliases = getAliases(clazz);
        if (aliases == null) {
            aliases = new PropertyAliases();
            classAliasMap.put(clazz, aliases);
        }
        return aliases;
    }

    public static void createAlias(@NotNull Class clazz, @NotNull String alias, @NotNull String... propertyName) {
        PropertyAliases aliases = createAliases(clazz);
        aliases.createAlias(alias, propertyName);
    }

    @Nullable
    public static String[] getPropertyName(@NotNull Class clazz, @NotNull String alias) {
        PropertyAliases aliases = getAliases(clazz);
        if (aliases == null) {
            return null;
        }
        return aliases.getPropertyName(alias);
    }

    private PropertyAliases() { }

    private void createAlias(@NotNull String alias, @NotNull String... propertyName) {
        if (propertyName.length == 0) {
            throw new IllegalArgumentException("propertyName cannot be 0 length array.");
        }
        aliasMap.put(alias.toLowerCase(), propertyName);
    }

    private String[] getPropertyName(@NotNull String alias) {
        return aliasMap.get(alias.toLowerCase());
    }
}
