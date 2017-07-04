/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties;

import pluginbase.messages.PluginBaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * A storage mechanism for {@link ValueProperty} values.
 * <br>
 * Essentially this class allows you to map values to {@link ValueProperty} objects.
 * <br>
 * Generally a Properties object should have some way to associate {@link ValueProperty} objects it will handle.
 */
public interface Properties {

    /**
     * Flushes any properties existing in memory into any kind of persistence mechanism if existing.
     * <br>
     * Some {@link Properties} may not take advantage of this in which case this method will simply do nothing.
     *
     * @throws PluginBaseException if attempting to do this raises runs into any problems.
     */
    void flush() throws PluginBaseException;

    /**
     * Reloads any values from any kind of persistence mechanism (if existing) into memory.
     * <br>
     * Some {@link Properties} may not take advantage of this in which case this method will simply do nothing.
     *
     * @throws PluginBaseException if attempting to do this raises runs into any problems.
     */
    void reload() throws PluginBaseException;

    /**
     * Gets the value for the specified property.
     *
     * @param property the property to retrieve the value for.
     * @param <T> the type of the property value.
     * @return the value of the property or null if no value assigned.
     * @throws IllegalArgumentException if the given property is not associated with this Properties object.
     */
    @Nullable
    <T> T get(@NotNull final SimpleProperty<T> property) throws IllegalArgumentException;

    /**
     * Gets the value for the specified property.
     *
     * @param property the property to retrieve the value for.
     * @param <T> the type of the property value.
     * @return the value of the property or null if no value assigned.
     * @throws IllegalArgumentException if the given property is not associated with this Properties object.
     */
    @Nullable
    <T> List<T> get(@NotNull final ListProperty<T> property) throws IllegalArgumentException;

    /**
     * Gets the value for the specified property.
     *
     * @param property the property to retrieve the value for.
     * @param <T> the type of the property value.
     * @return the value of the property or null if no value assigned.
     * @throws IllegalArgumentException if the given property is not associated with this Properties object.
     */
    @Nullable
    <T> Map<String, T> get(@NotNull final MappedProperty<T> property) throws IllegalArgumentException;

    /**
     * Gets the value for the specified key of the specified map property.
     *
     * @param property the property to retrieve the value for.
     * @param key the specific key of the map to retrieve the value for.
     * @param <T> the type of the property value.
     * @return the value of the property or null if no value assigned.
     * @throws IllegalArgumentException if the given property is not associated with this Properties object.
     */
    @Nullable
    <T> T get(@NotNull final MappedProperty<T> property, @NotNull final String key) throws IllegalArgumentException;

    /**
     * Retrieves a nested set of properties that belongs to this Properties object.
     *
     * @param property the nested property to retrieve.
     * @return the nested properties.
     * @throws IllegalArgumentException if the given property is not nested within this Properties object.
     */
    @NotNull
    NestedProperties get(@NotNull final NestedProperty property) throws IllegalArgumentException;

    /**
     * Sets the value for the specified property.
     *
     * @param property the property to set the value for.
     * @param value the new value for the property.
     * @param <T> the type of the property value.
     * @return true if the property was successfully set.
     * @throws IllegalArgumentException if the given property is not nested within this Properties object.
     */
    <T> boolean set(@NotNull final SimpleProperty<T> property, T value) throws IllegalArgumentException;

    /**
     * Sets the value for the specified property.
     *
     * @param property the property to set the value for.
     * @param value the new value for the property.
     * @param <T> the type of the property value.
     * @return true if the property was successfully set.
     * @throws IllegalArgumentException if the given property is not nested within this Properties object.
     */
    <T> boolean set(@NotNull final ListProperty<T> property, List<T> value) throws IllegalArgumentException;

    /**
     * Sets the value for the specified property.
     *
     * @param property the property to set the value for.
     * @param value the new value for the property.
     * @param <T> the type of the property value.
     * @return true if the property was successfully set.
     * @throws IllegalArgumentException if the given property is not nested within this Properties object.
     */
    <T> boolean set(@NotNull final MappedProperty<T> property, Map<String, T> value) throws IllegalArgumentException;

    /**
     * Sets the value for the specified key of the specified map property.
     *
     * @param property the property to set the value for.
     * @param key the key of the map to set the value for.
     * @param value the new value for the property.
     * @param <T> the type of the property value.
     * @return true if the property was successfully set.
     * @throws IllegalArgumentException if the given property is not nested within this Properties object.
     */
    <T> boolean set(@NotNull final MappedProperty<T> property, @NotNull final String key, T value) throws IllegalArgumentException;

    /**
     * Sets the property validator for the specified value property.
     * <br>
     * This allows external objects to specify what constitutes as a valid value for a property in this properties object.
     *
     * @param property the property to set a validator for.
     * @param validator the validator for the property.
     * @param <T> the type of the property.
     */
    <T> void setPropertyValidator(@NotNull final ValueProperty<T> property, @NotNull final PropertyValidator<T> validator);

    /**
     * Determines if the specified value is a valid value for a specified property fo this properties object.
     *
     * @param property the property to validate a value for.
     * @param value the value to validate.
     * @param <T> the type of the property and value.
     * @return true if the value is valid.
     */
    <T> boolean isValid(@NotNull final ValueProperty<T> property, @Nullable final T value);

    /**
     * Adds an observer to this properties object.
     *
     * @param observer The observer that wishes to be notified of property changes in this object.
     * @return True if the observer has not already been added.
     */
    boolean addObserver(@NotNull final Observer observer);

    /**
     * Removes an observer from this properties object.
     *
     * @param observer The observer that no longer wishes to be notified of property changes in this object.
     * @return True if the observer existed and was removed.
     */
    boolean deleteObserver(@NotNull final Observer observer);
}
