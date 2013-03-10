/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Message} preset with arguments to fill in any instances of {@code %s}.
 * <p/>
 * Can be used in cases where you are required to return a message of some sort and it is otherwise impossible to
 * return a localized message due to also requiring arguments.
 * <p/>
 * See {@link Message#bundleMessage(Message, Object...)} for creation of these bundled messages.
 */
public class BundledMessage {

    @NotNull
    private final Message message;
    @NotNull
    private final Object[] args;

    BundledMessage(@NotNull final Message message, @NotNull final Object... args) {
        this.message = message;
        this.args = args;
    }

    /**
     * Gets the localization message for this bundle.
     *
     * @return the localization message for this bundle.
     */
    @NotNull
    public final Message getMessage() {
        return message;
    }

    /**
     * Gets the arguments for the bundled message.
     *
     * @return the arguments for the bundled message.
     */
    @NotNull
    public final Object[] getArgs() {
        return args;
    }
}
