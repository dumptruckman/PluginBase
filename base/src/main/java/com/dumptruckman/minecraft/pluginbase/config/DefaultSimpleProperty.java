package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;

public class DefaultSimpleProperty<T> extends DefaultProperty<T> implements SimpleProperty<T> {

    private final T def;

    public DefaultSimpleProperty(Class<T> type, String path, T def, List<String> comments, List<String> aliases,
                                 PropertySerializer<T> serializer, PropertyValidator validator, Message description,
                                 boolean deprecated, boolean defaultIfMissing) {
        super(type, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
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
