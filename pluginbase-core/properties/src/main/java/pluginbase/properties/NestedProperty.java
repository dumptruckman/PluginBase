/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties;

/**
 * Represents a property that contains an additional set of properties.
 * <br>
 * See {@link pluginbase.properties.PropertyFactory} for creating properties.
 *
 * @param <T> the class that represents the nested set of properties.
 */
public interface NestedProperty<T extends NestedProperties> extends Property<T> { }
