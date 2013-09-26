/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.builders;

import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.properties.PropertyValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class DefaultValidator<T> implements PropertyValidator<T> {

    @Override
    public boolean isValid(@Nullable final T obj) {
        return true;
    }

    @NotNull
    @Override
    public Message getInvalidMessage() {
        return Messages.BLANK;
    }
}
