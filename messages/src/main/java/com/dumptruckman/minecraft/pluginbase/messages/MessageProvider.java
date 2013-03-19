/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages;

import com.dumptruckman.minecraft.pluginbase.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Indicates that localized messages can be retrieved from this class.
 * <p/>
 * See {@link Messages#loadMessages(Localizable, java.io.File, java.util.Locale)} for a simple flat file implementation.
 */
public interface MessageProvider {

    /** The default locale. */
    @NotNull
    Locale DEFAULT_LOCALE = Locale.ENGLISH;
    /** The default message/language file */
    @NotNull
    String DEFAULT_LANGUAGE_FILE_NAME = "english.txt";

    /**
     * Returns a message (as {@link String}) for the specified key (as {@link com.dumptruckman.minecraft.pluginbase.messages.Messages}).
     * <p/>
     * This is a localized message.
     *
     * @param key  the message key.
     * @param args arguments for String.format().
     * @return the localized message.
     */
    @NotNull
    String getLocalizedMessage(@NotNull final Message key, @NotNull final Object... args);

    /**
     * MessageProvider requires a method to acquire a logger due to the needs of the default implementations of the
     * classes that utilize the MessageProvider interface to log messages.
     *
     * @return a PluginLogger for this MessageProvider.
     */
    @NotNull
    PluginLogger getLog();
}

