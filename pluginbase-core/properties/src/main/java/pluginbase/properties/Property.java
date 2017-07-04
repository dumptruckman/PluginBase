/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a property of some type.
 * <br>
 * This class is the basis of many others and should typically not be used directly.
 * <br>
 * See {@link pluginbase.properties.PropertyFactory} for creating properties.
 *
 * @param <T> the type of the property.
 */
public interface Property<T> {

    /**
     * Retrieves the key for this property.
     * <br>
     * If this property is persisted this should be the name to reference the value within the persistence.
     *
     * @return the key for this property.
     */
    @NotNull
    String getName();

    /**
     * Retrieves the type of this property.
     *
     * @return the type of this property.
     */
    @NotNull
    Class<T> getType();

    /**
     * Retrieves the comment for this property.
     *
     * @return the comment for this property.
     */
    @NotNull
    List<String> getComments();
}
