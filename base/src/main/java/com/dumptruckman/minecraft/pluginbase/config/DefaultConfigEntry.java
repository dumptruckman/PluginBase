package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;

class DefaultConfigEntry<T> implements ConfigEntry<T> {

    private String path;
    private T def;
    private final List<String> comments;
    private Class<T> type;
    private EntrySerializer<T> serializer;
    private EntryValidator validator;

    public DefaultConfigEntry(Class<T> type, String path, T def, List<String> comments,
                              EntrySerializer<T> serializer, EntryValidator validator) {
        this.path = path;
        this.def = def;
        this.comments = comments;
        this.type = type;
        this.serializer = serializer;
        this.validator = validator;
    }

    public String getName() {
        return this.path;
    }

    public Class<T> getType() {
        return this.type;
    }

    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    public T getDefault() {
        return this.def;
    }

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    public List<String> getComments() {
        return this.comments;
    }

    public boolean isValid(Object obj) {
        return validator.isValid(obj);
    }

    public Message getInvalidMessage() {
        return validator.getInvalidMessage();
    }

    @Override
    public Object serialize(T value) {
        return serializer.serialize(value);
    }

    @Override
    public T deserialize(Object o) {
        return serializer.deserialize(o);
    }
}
