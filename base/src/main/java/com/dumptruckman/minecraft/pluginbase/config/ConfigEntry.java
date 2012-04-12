package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;

public interface ConfigEntry<T> {

    /**
     * Retrieves the path for a config option.
     *
     * @return The path for a config option.
     */
    String getName();

    Class<T> getType();

    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    T getDefault();

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    List<String> getComments();

    boolean isValid(Object obj);

    Object serialize(T value);

    T deserialize(Object o);

    Message getInvalidMessage();

    Message getDescription();
}
