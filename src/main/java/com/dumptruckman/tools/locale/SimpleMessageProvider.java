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
     * Formats a string by replacing ampersand with the Section symbol and %n with the corresponding args
     * object where n = argument index + 1.
     *
     * @param string String to format.
     * @param args   Arguments to pass in via %n.
     * @return The formatted string.
     */
    public String format(String string, Object... args) {
        // Replaces & with the Section character
        string = string.replaceAll("(?i)&([A-FK0-9])", Font.SECTION_SYMBOL + "$1");
        // If there are arguments, %n notations in the message will be
        // replaced
        if (args != null) {
            for (int j = 0; j < args.length; j++) {
                string = string.replace("%" + (j + 1), args[j].toString());
            }
        }
        // Format for locale
        string = String.format(this.locale, string);
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
            if (!Messages.messages.containsKey(key.toLowerCase())) {
                this.language.set(key, null);
            }
        }

        // Get language from file, if any is missing, set it to default.
        for (Map.Entry<String, Message> messageEntry : Messages.messages.entrySet()) {
            List<String> messageList = this.language.getStringList(messageEntry.getKey());
            if (messageList == null || messageList.isEmpty()) {
                messageList = messageEntry.getValue().getDefault();
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

