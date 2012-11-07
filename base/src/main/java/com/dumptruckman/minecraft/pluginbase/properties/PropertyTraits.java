package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;

interface PropertyTraits<T> {

    /**
     * Retrieves the path for a config option.
     *
     * @return The path for a config option.
     */
    String getName();

    /**
     * Retrieves the type of entry this is.
     *
     * @return the type of entry this is.
     */
    Class<T> getType();

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    List<String> getComments();
}
