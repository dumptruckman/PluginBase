/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages.messaging;

import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.LocalizablePlugin;

import java.io.File;
import java.util.Locale;

/**
 * Indicates that this class has a Messager available for sending messages to users.
 */
public interface Messaging extends LocalizablePlugin {

    /**
     * Gets the messager.
     *
     * @return the messager.
     */
    @NotNull
    Messager getMessager();

    /**
     * Loads the messages from the give file using the given locale.
     *
     * @param loader the configuration loader to load messages with.
     * @param locale the locale used to format the messages.
     */
    void loadMessages(@NotNull ConfigurationLoader loader, @NotNull Locale locale);
}
