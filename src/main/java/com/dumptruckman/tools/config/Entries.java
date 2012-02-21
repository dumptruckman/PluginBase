package com.dumptruckman.tools.config;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Entries {

    static final Set<ConfigEntry> entries = new HashSet<ConfigEntry>();

    public static void registerConfig(Class configClass) {
        Field[] fields = configClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isInstance(ConfigEntry.class)) {
                field.setAccessible(true);
                try {
                    entries.add((ConfigEntry) field.get(null));
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addEntry(ConfigEntry entry) {
        entries.add(entry);
    }
}
