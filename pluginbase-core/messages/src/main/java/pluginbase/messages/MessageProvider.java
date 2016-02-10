/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Indicates that localized messages can be retrieved from this class.
 * <p/>
 * See {@link Messages#loadMessages(LocalizablePlugin, ninja.leaping.configurate.loader.ConfigurationLoader, java.util.Locale)} for a simple flat file implementation.
 */
public interface MessageProvider {

    /** The default locale. */
    @NotNull
    Locale DEFAULT_LOCALE = Locale.US;

    /**
     * Returns a message (as {@link String}) for the specified key (as {@link pluginbase.messages.Messages}).
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
     * Returns a message (as {@link String}) for the specified key (as {@link pluginbase.messages.Messages}).
     * <p/>
     * This is a localized message.
     *
     * @param key  the message key.
     * @param args arguments for String.format().
     * @return the localized message.
     */
    @NotNull
    String getLocalizedMessage(@NotNull final Object[] key, @NotNull final Object... args);

    /**
     * Returns the plugin this message provider provides messages for.
     *
     * @return the plugin this message provider provides messages for.
     */
    @NotNull
    LocalizablePlugin getPlugin();
}

