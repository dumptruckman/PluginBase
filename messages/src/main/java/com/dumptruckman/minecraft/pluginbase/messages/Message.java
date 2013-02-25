/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages;

import org.jetbrains.annotations.NotNull;

/**
 * A localization key and its defaults.
 * <p/>
 * The key represents the location of the localized strings in a language file.
 * <br/>
 * The default is what should populate the localization file by default.
 */
public final class Message {

    @NotNull
    private final String def;

    private final String key;

    /**
     * Creates a new localized message.
     * <p/>
     * This should be defined as a final static object (constant).
     * <p/>
     * The class that contains the definition must be
     * registered with {@link Messages#registerMessages(MessageProviding, Class)} prior to creating a {@link SimpleMessageProvider}
     * in order to have the default messages populate the language file.
     *
     * @param key The localization key for this message.
     * @param def The default message in whatever your plugin's primary language is.
     */
    public Message(@NotNull final String key, @NotNull final String def) {
        this.key = key;
        this.def = def;
    }

    Message(@NotNull final String def) {
        this.key = null;
        this.def = def;
    }

    /**
     * The default message in whatever your plugin's primary language is.
     *
     * @return The default non-localized messages.
     */
    @NotNull
    public String getDefault() {
        return def;
    }

    /**
     * The localization key for the message.
     *
     * @return The localization key for the message.
     */
    @NotNull
    public String getKey() {
        return key;
    }
}
