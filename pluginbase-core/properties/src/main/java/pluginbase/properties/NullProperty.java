/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties;

/**
 * This represents a special type of property that has no value and only typically exists to add section comments.
 * <br>
 * See {@link pluginbase.properties.PropertyFactory} for creating properties.
 */
public interface NullProperty extends ValueProperty<Null> { }
