/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of MessageProvider.
 */
public class SimpleMessageProvider implements MessageProvider {

    private final HashMap<Message, List<String>> messages;
    private final PluginBase plugin;

    private Locale locale = DEFAULT_LOCALE;
    private String languageFileName = DEFAULT_LANGUAGE_FILE_NAME;
    private FileConfiguration language = null;

    public SimpleMessageProvider(PluginBase plugin) {
        this.plugin = plugin;
        messages = new HashMap<Message, List<String>>();
    }
    
    private List<String> _getMessages(Message key) {
        List<String> messageList = this.messages.get(key);
        if (messageList == null) {
            Logging.warning("There is not language entry for " + key.getPath() + ".  Was it registered?");
            return new ArrayList<String>();
        }
        return messageList;
    }

    /**
     * Formats a list of strings by passing each through {@link #format(String, Object...)}.
     *
     * @param strings List of strings to format.
     * @param args    Arguments to pass in via %n.
     * @return List of formatted strings.
     */
    public List<String> format(List<String> strings, Object... args) {
        List<String> formattedStrings = new ArrayList<String>();
        for (String string : strings) {
            formattedStrings.add(format(string, args));
        }
        return formattedStrings;
    }

    /**
     * Formats a string by replacing ampersand with the Section symbol and %s with the corresponding args
     * object in a fashion similar to String.format().
     *
     * @param string String to format.
     * @param args   Arguments to pass in via %n.
     * @return The formatted string.
     */
    public String format(String string, Object... args) {
        // Replaces & with the Section character
        string = ChatColor.translateAlternateColorCodes('&', string);
        // If there are arguments, %n notations in the message will be
        // replaced
        /*if (args != null) {
            for (int j = 0; j < args.length; j++) {
                if (args[j] == null) {
                    args[j] = "NULL";
                }
                string = string.replace("%" + (j + 1), args[j].toString());
            }
        }*/
        // Format for locale
        // TODO need a fix for this when language vars are not passed in as args.
        try {
            string = String.format(locale, string, args);
        } catch (IllegalFormatException e) {
            Logging.warning("Language string format is incorrect: " + string);
        }
        return string;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(Message key, Object... args) {
        List<String> message = _getMessages(key);
        if (message.isEmpty()) {
            return "";
        }
        return format(message.get(0), args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMessages(Message key, Object... args) {
        return format(_getMessages(key), args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    @Override
    public void setLanguage(String languageFileName) {
        this.languageFileName = languageFileName;
        loadLanguage();
    }

    protected final void loadLanguage() {
        File languageFile = new File(this.plugin.getDataFolder(), this.languageFileName);
        this.language = YamlConfiguration.loadConfiguration(languageFile);
        // Prune file
        for (String key : this.language.getKeys(true)) {
            if (!(this.language.get(key) instanceof List)) {
                continue;
            }
            if (!Messages.messages.containsKey(key.toLowerCase())) {
                Logging.finer("Removing unused language: %s", key);
                this.language.set(key, null);
            }
        }

        // Get language from file, if any is missing, set it to default.
        for (Map.Entry<String, Message> messageEntry : Messages.messages.entrySet()) {
            if (messageEntry.getValue().getPath() == null) {
                continue;
            }
            List<String> messageList = this.language.getStringList(messageEntry.getKey());
            boolean changed = false;
            if (messageList == null || messageList.isEmpty()) {
                messageList = messageEntry.getValue().getDefault();
                changed = true;
            }
            for (int i = 0; i < messageList.size(); i++) {
                final String message = messageList.get(i);
                final String newMessage = message.replaceAll("%\\d", "%s");;
                if (!newMessage.equals(message)) {
                    messageList.set(i, newMessage);
                    changed = true;
                }
            }
            if (changed) {
                this.language.set(messageEntry.getKey().toLowerCase(), messageList);
            }
            this.messages.put(messageEntry.getValue(), messageList);
        }
        String ls = System.getProperty("line.separator");
        this.language.options().header("You may insert color into the strings by preceding the color code with &.  "
                + "Example: &cThis is red" + ls + ls + "%<number> represents places where"
                + " data will be filled in by the plugin." + ls + ls + "To create a new "
                + "language file, change the file name in config.yml and type /" + this.plugin.getCommandPrefix()
                + " reload" + ls + "This will create a new file for you to edit");
        try {
            this.language.save(languageFile);
        } catch (IOException e) {
            Logging.severe("Could not save language file!");
            e.printStackTrace();
        }
    }
}

