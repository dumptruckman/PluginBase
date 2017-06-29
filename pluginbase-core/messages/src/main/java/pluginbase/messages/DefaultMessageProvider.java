/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.Nullable;
import pluginbase.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Locale;

class DefaultMessageProvider implements MessageProvider {

    @NotNull
    private final Locale locale;

    @NotNull
    private final ConfigurationNode messages;

    @NotNull
    private final LocalizablePlugin plugin;

    public DefaultMessageProvider(@NotNull final LocalizablePlugin localizablePlugin,
                                  @NotNull final ConfigurationLoader loader,
                                  @NotNull final Locale locale) {
        this.locale = locale;
        this.plugin = localizablePlugin;
        messages = load(localizablePlugin, loader);
        prune(localizablePlugin, messages);
        save(loader, messages);
    }

    private void save(@NotNull final ConfigurationLoader loader, @NotNull final ConfigurationNode language) {
        try {
            loader.save(language);
        } catch (IOException e) {
            getLog().warning("Problem saving current language file");
            e.printStackTrace();
        }
    }

    private void prune(@NotNull final LocalizablePlugin localizable, @NotNull final ConfigurationNode language){
        // Prune file
        final Iterator<? extends ConfigurationNode> it = language.getChildrenList().iterator();
        while (it.hasNext()) {
            final Object[] key = it.next().getPath();
            if (!Messages.containsMessageKey(localizable, key)) {
                getLog().finer("Removing unused language: %s", key);
                it.remove();
            }
        }
    }

    @NotNull
    private ConfigurationNode load(@NotNull final LocalizablePlugin localizable, @NotNull final ConfigurationLoader loader) {
        ConfigurationNode language;
        try {
            language = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            language = loader.createEmptyNode();
        }

        // TODO check if languages is empty?

        for (final Object[] key : Messages.getMessageKeys(localizable)) {
            final Message message = Messages.getMessage(localizable, key);
            ConfigurationNode node = language.getNode(key);
            if (message != null && node.isVirtual()) {
                if (node.isVirtual()) {
                    node.setValue(message.getDefault());
                    getLog().finest("Created new message in language file: %s", message);
                } else if(Message.countArgs(node.getString())
                        != message.getArgCount()) {
                    node.setValue(message.getDefault());
                    getLog().warning("The message for '%s' in the current language file does not have the correct amount of arguments (%s).  The default will be used.", key, message.getArgCount());
                }
            }
        }
        return language;
    }

    private String _getMessage(@NotNull final Object[] key) {
        ConfigurationNode node = this.messages.getNode(key);
        if (node.isVirtual()) {
            String keyString = Arrays.toString(key);
            getLog().warning("There is not language entry for %s.  Was it registered?", keyString);
            return keyString;
        }
        return node.getString();
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
    public String getLocalizedMessage(@NotNull final Object[] key, @NotNull final Object... args) {
        final String message = _getMessage(key);
        return formatMessage(key, message, args);
    }

    private String formatMessage(@Nullable Object[] key, @NotNull String message, @NotNull final Object... args) {
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
