package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;

public class DefaultSimpleEntry<T> extends DefaultEntry<T> implements SimpleEntry<T> {

    private final T def;

    public DefaultSimpleEntry(Class<T> type, String path, T def, List<String> comments,
                              EntrySerializer<T> serializer, EntryValidator validator, Message description,
                              boolean deprecated, boolean defaultIfMissing) {
        super(type, path, comments, serializer, validator, description, deprecated, defaultIfMissing);
        this.def = def;
    }
    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    public T getDefault() {
        return this.def;
    }
}
