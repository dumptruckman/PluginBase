/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages;

import pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A class used for registering and keeping track of localized messages.
 */
public class Messages {

    private Messages() {
        throw new AssertionError();
    }

    @NotNull
    private static final Map<Localizable, Map<String, Message>> messages = new HashMap<Localizable, Map<String, Message>>();

    @NotNull
    static Set<String> getMessageKeys(@NotNull final Localizable localizable) {
        if (!messages.containsKey(localizable)) {
            throw new IllegalArgumentException("Provider has no registered messages.");
        }
        return messages.get(localizable).keySet();
    }

    @Nullable
    static Message getMessage(@NotNull final Localizable localizable, @Nullable final String key) {
        if (!messages.containsKey(localizable)) {
            throw new IllegalArgumentException("Provider has no registered messages.");
        }
        if (key == null) {
            return BLANK;
        }
        return messages.get(localizable).get(key);
    }

    static boolean containsMessageKey(@NotNull final Localizable localizable, @NotNull final String key) {
        if (!messages.containsKey(localizable)) {
            throw new IllegalArgumentException("Provider has no registered messages.");
        }
        return messages.get(localizable).containsKey(key);
    }

    /** A message with no text. */
    @NotNull public final static Message BLANK = new Message("");

    /** Used for wrapping regular exceptions into a PluginBaseException. */
    @NotNull public final static Message EXCEPTION = Message.createMessage("generic.exception", Theme.PLAIN + "%s");

    /** Used for wrapping PluginBaseExceptions into a PluginBaseException. */
    @NotNull public final static Message CAUSE_EXCEPTION = Message.createMessage("generic.cause_exception", Theme.PLAIN + "Caused by: %s");

    /** A message of general success */
    @NotNull public final static Message SUCCESS = Message.createMessage("generic.success", Theme.SUCCESS + "[SUCCESS]");

    /** A message of general error */
    @NotNull public final static Message ERROR = Message.createMessage("generic.error", Theme.ERROR + "[ERROR]");

    /** A message of general loading error */
    @NotNull public final static Message COULD_NOT_LOAD = Message.createMessage("generic.could_not_load", "Could not load: %s");
    /** A message of general loading error */
    @NotNull public final static Message COULD_NOT_SAVE = Message.createMessage("generic.could_not_save", "Could not save: %s");

    /**
     * Registers all of the messages in a given class and all inner classes to the localizable object.
     * <p/>
     * Messages are defined with the {@link Message} class and should be declared as static and final (constant).
     * Their access modifier is not important and should be set as your needs dictate.
     * <p/>
     * This method will import all of the Messages defined per the above guide lines that exist in the class.
     * Those messages imported will be linked to the given localizable object.
     *
     * @param localizable the "owner" of the message.  Typically this a plugin who will be using the messages.
     * @param clazz the class that contains the definition of the messages.
     */
    public static void registerMessages(@NotNull final Localizable localizable, @NotNull final Class clazz) {
        if (clazz.equals(Messages.class)) {
            return;
        }
        if (!messages.containsKey(localizable)) {
            messages.put(localizable, new HashMap<String, Message>());
        }
        final Map<String, Message> messages = Messages.messages.get(localizable);
        if (!messages.containsKey(SUCCESS.getKey())) {
            messages.put(SUCCESS.getKey(), SUCCESS);
        }
        if (!messages.containsKey(ERROR.getKey())) {
            messages.put(ERROR.getKey(), ERROR);
        }
        if (!messages.containsKey(EXCEPTION.getKey())) {
            messages.put(EXCEPTION.getKey(), EXCEPTION);
        }
        if (!messages.containsKey(CAUSE_EXCEPTION.getKey())) {
            messages.put(CAUSE_EXCEPTION.getKey(), CAUSE_EXCEPTION);
        }

        final Field[] f1 = clazz.getDeclaredFields();
        final Field[] f2 = clazz.getFields();
        final Field[] fields = new Field[f1.length + f2.length];
        System.arraycopy(f1, 0, fields, 0, f1.length);
        System.arraycopy(f2, 0, fields, f1.length, f2.length);
        for (final Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())
                    && Modifier.isFinal(field.getModifiers())
                    && Message.class.isAssignableFrom(field.getType())) {
                final boolean access = field.isAccessible();
                if (!access) {
                    field.setAccessible(true);
                }
                try {
                    final Message message = (Message) field.get(null);
                    if (message == null) {
                        continue;
                    }
                    if (!messages.containsKey(message.getKey())) {
                        messages.put(message.getKey(), message);
                    }
                } catch (IllegalAccessException e) {
                    Logging.warning("Could not register language message '%s' from class '%s'", field, clazz);
                }
                if (!access) {
                    field.setAccessible(false);
                }
            }
        }

        for (Class<?> c : clazz.getDeclaredClasses()) {
            registerMessages(localizable, c);
        }
    }

    /**
     * Loads the given language file into a new MessageProvider set to use the given locale.
     * <p/>
     * Any messages registered with {@link #registerMessages(Localizable, Class)} for the same Localizable object
     * should be present in this file.  If they are not previously present, they will be inserted with the default
     * message.  If any message is located in the file that is not registered as previously mentioned it will be
     * removed from the file.
     *
     * @param localizable the object that registered localizable messages.
     * @param languageFile the language file to load localized messages from.
     * @param locale the locale to use when formatting the messages.
     * @return a new MessagerProvider loaded with the messages from the given language file and locale.
     */
    @NotNull
    public static MessageProvider loadMessages(@NotNull final Localizable localizable,
                                               @NotNull final File languageFile,
                                               @NotNull final Locale locale) {
        return new DefaultMessageProvider(localizable, languageFile, locale);
    }
}

