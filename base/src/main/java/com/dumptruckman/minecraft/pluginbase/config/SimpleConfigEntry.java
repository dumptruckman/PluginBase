package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.locale.Messages;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleConfigEntry<T> implements ConfigEntry<T> {

    private String path;
    private T def;
    private final List<String> comments;
    private Class<T> type;

    public SimpleConfigEntry(Class<T> type, String path, T def, String... comments) {
        this.path = path;
        this.def = def;
        this.comments = new ArrayList<String>(Arrays.asList(comments));
        this.type = type;
        Entries.entries.add(this);
    }

    /**
     * Retrieves the path for a config option.
     *
     * @return The path for a config option.
     */
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
        return true;
    }

    public Message getInvalidMessage() {
        return Messages.BLANK;
    }

    @Override
    public Object serialize(T value) {
        return value;
    }

    @Override
    public T deserialize(Object o) {
        return getType().cast(o);
    }
}
