/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties;

import pluginbase.messages.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Used to validate the value of a property before setting it.
 */
public interface PropertyValidator<T> {

    /**
     * Checks to see if the value is valid based on the constraints of this PropertyValidator.
     *
     * @param value the value to check.
     * @return true if the value is valid.
     */
    boolean isValid(@Nullable final T value);

    /**
     * Retrieves the language to use when an invalid value is attempted to be used for the associated property.
     * This will return {@link pluginbase.messages.Messages#BLANK} by default.
     *
     * @return the language to use when an invalid value is attempted to be used for the associated property.
     */
    @NotNull
    Message getInvalidMessage();
}
