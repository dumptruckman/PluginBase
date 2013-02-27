/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents some type of property that represents a value.
 * <p/>
 * This property does not store the value, merely identifies it.
 * <p/>
 * See {@link com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory} for creating properties.
 *
 * @param <T> the type of the property.
 */
public interface ValueProperty<T> extends Property<T> {

    /**
     * Retrieves the default value for this property.
     *
     * @return The default value for this property or null if no default value.
     */
    @Nullable
    Object getDefault();

    /**
     * Retrieves the aliases for this property.
     * <p/>
     * Aliases are sometimes used when config entries are checked reflectively to offer an easy way to set
     * values via command.
     *
     * @return The aliases for this property.
     */
    @NotNull
    List<String> getAliases();

    /**
     * Verifies that the given object is a valid value for this property.
     *
     * @param obj the object to verify.
     * @return true if the object is a valid value for this property.
     */
    boolean isValid(@Nullable final T obj);

    /**
     * Indicates whether ot not this property is deprecated which will cause it to no longer default itself in any
     * forms of persistence or memory.
     *
     * @return true if property is deprecated.
     */
    boolean isDeprecated();

    /**
     * This defines whether or not the value should be defaulted when retrieved if their is no associated value for
     * this property in a {@link Properties} object.
     * <p/>
     * This does not, however, cause the value to be added to the {@link Properties}.
     * It will simply cause the default to be returned.
     *
     * @return true if value should be defaulted when missing.
     */
    boolean shouldDefaultIfMissing();

    /**
     * Gets the default serializer for this property if there is one.
     * <p/>
     * This will be used to serialize/deserialize the property if not replaced by the {@link Properties} implementation.
     *
     * @return The default serializer for this property or null if not specified.
     */
    @Nullable
    PropertySerializer<T> getDefaultSerializer();

    /**
     * Retrieves the description of this entry in the localizable {@link Message} form.
     * <p/>
     * This will return {@link com.dumptruckman.minecraft.pluginbase.messages.Messages#BLANK} by default.
     *
     * @return the description of this entry in in the localizable {@link Message} form.
     */
    @NotNull
    Message getDescription();
}
