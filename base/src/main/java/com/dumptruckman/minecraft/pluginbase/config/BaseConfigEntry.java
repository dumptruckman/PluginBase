package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;

public interface BaseConfigEntry<T> {

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

    /**
     * Verifies that the given object is a valid value for this entry.
     *
     * @param obj The object to verify.
     * @return True if the object is a valid value for this entry.
     */
    boolean isValid(Object obj);

    /**
     * Indicates whether ot not this entry is deprecated which will cause it to no longer default itself in the config
     * if missing.
     *
     * @return True if entry is deprecated.
     */
    boolean isDeprecated();

    /**
     * This defines whether or not the value should be defaulted when retrieved if the value is missing from the config.
     * This does not, however, cause the value to be add to the config.  It will simply cause the default to be
     * returned.
     *
     * @return True if value should be defaulted.
     */
    boolean shouldDefaultIfMissing();

    /**
     * Serializes the given value into a form that is acceptable by the Configuration being used.
     *
     * @param value The value to serialize.
     * @return The serialized form of the value.
     */
    Object serialize(T value);

    /**
     * Deserializes the given object from the form that is stored in the Configuration into a form that fits the type
     * of this Config Entry.
     *
     * @param o The value to deserialize.
     * @return The deserialized value.
     */
    T deserialize(Object o);

    /**
     * Retrieves the language to use when an invalid value is attempted to be used for this entry.
     * This will return {@link com.dumptruckman.minecraft.pluginbase.locale.Messages#BLANK} by default.
     *
     * @return the language to use when an invalid value is attempted to be used for this entry.
     */
    Message getInvalidMessage();

    /**
     * Retrieves the description of this entry in Message form.
     * This will return {@link com.dumptruckman.minecraft.pluginbase.locale.Messages#BLANK} by default.
     *
     * @return the description of this entry in Message form.
     */
    Message getDescription();
}
