package com.dumptruckman.minecraft.config;

import com.dumptruckman.minecraft.locale.Message;

import java.util.List;

public interface ConfigEntry<T> {

    /**
     * Retrieves the path for a config option.
     *
     * @return The path for a config option.
     */
    String getName();

    Class getType();

    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    Object getDefault();

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    List<String> getComments();

    //T get();

    //void set(T value);

    boolean isValid(Object obj);

    Message getInvalidMessage();
}
