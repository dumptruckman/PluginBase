/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages;

import org.jetbrains.annotations.Nullable;
import pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;

/**
 * A localization key and its defaults.
 * <p/>
 * The key represents the location of the localized strings in a language file.
 * <br/>
 * The default is what should populate the localization file by default.
 */
public interface Message {

    /**
     * Creates a new localized message.
     * <p/>
     * This should be defined as a final static object (constant).
     * <p/>
     * The class that contains the definition must be
     * registered with {@link Messages#registerMessages(LocalizablePlugin, Class)} prior to creating a {@link DefaultMessageProvider}
     * in order to have the default messages populate the language file.
     *
     * @param key The localization key for this message.
     * @param def The default message in whatever your plugin's primary language is.
     * @param additionalLines This param allows additional lines to be added to the message. This is optional and
     *                        multiline messages can be given in the original parameter by using line break characters.
     */
    static Message createMessage(@NotNull final String key, @NotNull final String def, final String... additionalLines) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(def);
        for (String line : additionalLines) {
            buffer.append("\n").append(line);
        }
        return new DefaultMessage(key, buffer.toString());
    }

    /**
     * Creates a new message that will not be localized.
     * <p/>
     * This is intended to be used sparingly or to provide a simpler api for 3rd parties. These messages will not have
     * an entry in any language files.
     *
     * @param message The non localized message.
     * @param additionalLines This param allows additional lines to be added to the message. This is optional and
     *                        multiline messages can be given in the original parameter by using line break characters.
     */
    static Message createStaticMessage(@NotNull final String message, final String... additionalLines) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(message);
        for (String line : additionalLines) {
            buffer.append("\n").append(line);
        }
        return new DefaultMessage(null, buffer.toString());
    }

    /**
     * Bundles a {@link Message} with preset arguments.
     * <p/>
     * Can be used in cases where you are required to return a message of some sort and it is otherwise impossible to
     * return a localized message due to also requiring arguments.
     *
     * @param message The localization message for the bundle.
     * @param args The arguments for the bundled message.
     */
    static BundledMessage bundleMessage(@NotNull final Message message, @NotNull final Object... args) {
        if (args.length != message.getArgCount()) {
            Logging.warning("Bundled message created without appropriate number of arguments!");
            for (final StackTraceElement e : Thread.currentThread().getStackTrace()) {
                Logging.warning(e.toString());
            }
        }
        return new BundledMessage(message, args);
    }

    /**
     * The default message in whatever your plugin's primary language is.
     *
     * @return The default non-localized messages.
     */
    @NotNull
    String getDefault();

    /**
     * The localization key for the message. Each object in the array represents a configuration node path.
     *
     * @return The localization key for the message.
     */
    @Nullable
    Object[] getKey();

    /**
     * Gets the number of expected arguments for this message.
     * <p/>
     * This is used to validate localized versions of this message to ensure they were given the appropriate
     * amount of arguments.
     *
     * @return the number of expected arguments for this message.
     */
    int getArgCount();
}
