/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages;

import org.jetbrains.annotations.Nullable;
import pluginbase.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

class DefaultMessageProvider implements MessageProvider {

    @NotNull
    private final Locale locale;

    @NotNull
    private final Properties messages;

    @NotNull
    private final LocalizablePlugin plugin;

    public DefaultMessageProvider(@NotNull final LocalizablePlugin localizablePlugin,
                                  @NotNull final File languageFile,
                                  @NotNull final Locale locale) {
        this.locale = locale;
        this.plugin = localizablePlugin;
        messages = getProperties(localizablePlugin, languageFile);
        pruneLanguage(localizablePlugin, messages);
        storeProperties(languageFile, messages);
    }

    private void storeProperties(@NotNull final File languageFile, @NotNull final Properties properties) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(languageFile);
            properties.store(writer,
                    "You may insert color into the strings by preceding the color code with &."
                            + "\nExample: &cThis is red\n\nAny place where there is %s represents data"
                            + " to be filled in by the plugin.\nMAKE SURE THESE REMAIN IN ANY REPLACEMENTS!");
            getLog().fine("Saved language file %s", languageFile);
        } catch (IOException e) {
            getLog().warning("Problem saving language file '%s'", languageFile);
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignore) { }
            }
        }
    }

    private void pruneLanguage(@NotNull final LocalizablePlugin localizable, @NotNull final Properties language){
        // Prune file
        final Iterator<Object> it = language.keySet().iterator();
        while (it.hasNext()) {
            final String key = it.next().toString();
            if (!Messages.containsMessageKey(localizable, key)) {
                getLog().finer("Removing unused language: %s", key);
                it.remove();
            }
        }
    }

    @NotNull
    private Properties getProperties(@NotNull final LocalizablePlugin localizable, @NotNull final File languageFile) {
        final Properties language = new Properties();
        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
                getLog().fine("Created language file %s", languageFile);
            } catch (IOException e) {
                getLog().warning("Problem creating language file '%s'", languageFile);
                e.printStackTrace();
            }
        }
        if (languageFile.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(languageFile);
                language.load(reader);
                getLog().fine("Loaded language file %s", languageFile);
            } catch (IOException e) {
                getLog().warning("Problem loading language file '%s'", languageFile);
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignore) { }
                }
            }
        }
        for (final String key : Messages.getMessageKeys(localizable)) {
            if (!language.containsKey(key)) {
                final Message message = Messages.getMessage(localizable, key);
                if (message != null) {
                    language.put(key, message.getDefault());
                    getLog().finest("Created new message in language file: %s", message);
                }
            } else {
                final Message message = Messages.getMessage(localizable, key);
                if (message != null && Message.countArgs(language.getProperty(key)) != message.getArgCount()) {
                    getLog().warning("The message for '%s' in the file '%s' does not have the correct amount of arguments (%s).  The default will be used.", key, languageFile, message.getArgCount());
                    language.put(key, message.getDefault());
                }
            }
        }
        return language;
    }

    private String _getMessage(@NotNull final String key) {
        final String message = this.messages.getProperty(key);
        if (message == null) {
            getLog().warning("There is not language entry for %s.  Was it registered?", key);
            return key;
        }
        return message;
    }

    @Override
    @NotNull
    public String getLocalizedMessage(@NotNull final Message key, @NotNull final Object... args) {
        if (key.getKey() == null) {
            return formatMessage(null, key.getDefault(), args);
        }
        return getLocalizedMessage(key.getKey(), args);
    }

    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final String key, @NotNull final Object... args) {
        final String message = _getMessage(key);
        return formatMessage(key, message, args);
    }

    private String formatMessage(@Nullable String key, @NotNull String message, @NotNull final Object... args) {
        try {
            return MessageUtil.formatMessage(locale, message, args);
        } catch (IllegalFormatException e) {
            getLog().warning("Language string format is incorrect: %s: %s", key, message);
            for (final StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                getLog().warning(ste.toString());
            }
            return message;
        }
    }

    @NotNull
    @Override
    public LocalizablePlugin getPlugin() {
        return plugin;
    }

    @NotNull
    public PluginLogger getLog() {
        return getPlugin().getLog();
    }
}
