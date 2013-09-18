package com.dumptruckman.minecraft.pluginbase.config;

/**
 * Implemented by helper classes that are used to validate property-changes.
 *
 * @param <T> The type of the property whose changes should be validated.
 * @see ObjectUsingValidator
 */
public interface Validator<T> {
    /**
     * Called when a property-change should be validated.
     *
     * @param property The name of the property.
     * @param newValue The new value.
     * @param oldValue The old value.
     * @return The value the property should be set to.
     * @throws ChangeDeniedException When the property-change was denied.
     */
    T validateChange(String property, T newValue, T oldValue) throws ChangeDeniedException;
}
