/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import org.jetbrains.annotations.NotNull;

public class BundledMessage {

    @NotNull
    private Message message;
    @NotNull
    private Object[] args;

    public BundledMessage(@NotNull final Message message, @NotNull final Object...args) {
        this.message = message;
        this.args = args;
    }

    @NotNull
    public Message getMessage() {
        return message;
    }

    @NotNull
    public Object[] getArgs() {
        return args;
    }
}
