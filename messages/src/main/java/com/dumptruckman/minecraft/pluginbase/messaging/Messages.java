/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;

/**
 * An enum containing all messages/strings used by PluginBase.
 */
public class Messages {

    @NotNull
    protected static final Properties messages = new Properties();

    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc
    @NotNull
    public final static Message BLANK = new Message(null, "");
    // END CHECKSTYLE-SUPPRESSION: Javadoc

    static void registerMessage(@NotNull Message message) {
        if (message.getKey() != null) {
            messages.put(message.getKey(), message.getDefault());
        }
    }
}

