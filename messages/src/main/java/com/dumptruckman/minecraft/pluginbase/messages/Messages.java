/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * A class used internally to store all of the in use Localization messages.
 */
public class Messages {

    @NotNull
    private static final Map<MessageProviding, Properties> messages = new HashMap<MessageProviding, Properties>();

    @NotNull
    public static Set<String> getMessageKeys(@NotNull final MessageProviding provider) {
        if (!messages.containsKey(provider)) {
            throw new IllegalArgumentException("Provider has no registered messages.");
        }
        return messages.get(provider).stringPropertyNames();
    }

    @Nullable
    public static String getDefaultMessage(@NotNull final MessageProviding provider, @Nullable final String key) {
        if (!messages.containsKey(provider)) {
            throw new IllegalArgumentException("Provider has no registered messages.");
        }
        if (key == null) {
            return BLANK.getDefault();
        }
        return messages.get(provider).getProperty(key);
    }

    public static boolean containsMessageKey(@NotNull final MessageProviding provider, @NotNull final String key) {
        if (!messages.containsKey(provider)) {
            throw new IllegalArgumentException("Provider has no registered messages.");
        }
        return messages.get(provider).containsKey(key);
    }

    @NotNull public final static Message BLANK = new Message("");

    /** A message of general success */
    @NotNull public final static Message SUCCESS = new Message("generic.success", "&a[SUCCESS]&f");

    /** A message of general error */
    @NotNull public final static Message ERROR = new Message("generic.error", "&c[ERROR]&f");

    public static void registerMessages(@NotNull final MessageProviding provider, @NotNull final Class clazz) {
        if (clazz.equals(Messages.class)) {
            return;
        }
        if (!messages.containsKey(provider)) {
            messages.put(provider, new Properties());
        }
        final Properties props = messages.get(provider);
        if (!props.containsKey(SUCCESS.getKey())) {
            props.setProperty(SUCCESS.getKey(), SUCCESS.getDefault());
        }
        if (!props.containsKey(ERROR.getKey())) {
            props.setProperty(ERROR.getKey(), ERROR.getDefault());
        }

        final Field[] fields = clazz.getDeclaredFields();
        for (final Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && Message.class.isAssignableFrom(field.getType())) {
                final boolean access = field.isAccessible();
                if (!access) {
                    field.setAccessible(true);
                }
                try {
                    final Message message = (Message) field.get(null);
                    if (message == null) {
                        continue;
                    }
                    if (!props.containsKey(message.getKey())) {
                        props.put(message.getKey(), message.getDefault());
                    }
                } catch (IllegalAccessException e) {
                    Logging.warning("Could not register language message: %s", field);
                }
                if (!access) {
                    field.setAccessible(false);
                }
            }
        }
    }
}

