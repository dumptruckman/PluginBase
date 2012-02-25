package com.dumptruckman.tools.locale;

import com.dumptruckman.tools.plugin.PluginBase;
import com.dumptruckman.tools.util.Font;
import com.dumptruckman.tools.util.Logging;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
     * Formats a string by replacing ampersand with the Section symbol and %n with the corresponding args
     * object where n = argument index + 1.
     *
     * @param string String to format.
     * @param args   Arguments to pass in via %n.
     * @return The formatted string.
     */
    public String format(String string, Object... args) {
        // Replaces & with the Section character
        string = string.replaceAll("(&([a-fA-FkK0-9]))", Font.SECTION_SYMBOL + "$2");
        string = String.format(this.locale, string);
        // If there are arguments, %n notations in the message will be
        // replaced
        if (args != null) {
            for (int j = 0; j < args.length; j++) {
                string = string.replace("%" + (j + 1), args[j].toString());
            }
        }
        return string;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(Message key, Object... args) {
        return format(messages.get(key).get(0), args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMessages(Message key, Object... args) {
        System.out.println();
        return format(this.messages.get(key), args);
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
        for (String key : this.language.getKeys(false)) {
            System.out.println("found key: " + key);
            if (!Messages.messages.containsKey(key)) {
                System.out.println("key not message");
                this.language.set(key, null);
            }
        }

        System.out.println("asdf");
        System.out.println(Messages.getMessages());
        System.out.println("asdf");
        // Get language from file, if any is missing, set it to default.
        for (Map.Entry<String, Message> messageEntry : Messages.messages.entrySet()) {
            System.out.println("looking");
            List<String> messageList = this.language.getStringList(messageEntry.getKey());
            System.out.println(messageList);
            if (messageList == null) {
                messageList = messageEntry.getValue().getDefault();
                this.language.set(messageEntry.getKey(), messageList);
            }
            System.out.println(messageList);
            this.messages.put(messageEntry.getValue(), messageList);
        }
        try {
            this.language.save(languageFile);
        } catch (IOException e) {
            Logging.severe("Could not save language file!");
            e.printStackTrace();
        }
    }
}

