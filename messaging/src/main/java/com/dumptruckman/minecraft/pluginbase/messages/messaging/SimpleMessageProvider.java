package com.dumptruckman.minecraft.pluginbase.messages.messaging;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.ChatColor;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

public class SimpleMessageProvider extends Messages implements MessageProvider {

    @NotNull
    private Locale locale = DEFAULT_LOCALE;

    @NotNull
    private final File dataFolder;

    @Nullable
    private volatile String languageFileName = null;
    @NotNull
    private volatile Properties messages = new Properties();

    public SimpleMessageProvider(@NotNull final File dataFolder) {
        this.dataFolder = dataFolder;
    }

    private String _getMessage(@NotNull final Message key) {
        final String message = this.messages.getProperty(key.getKey());
        if (message == null) {
            Logging.warning("There is not language entry for %s.  Was it registered?", key.getKey());
            return "";
        }
        return message;
    }

    @NotNull
    private Properties getProperties(@NotNull final String languageFileName){
        final Properties language = new Properties();
        final File languageFile = new File(dataFolder, languageFileName);
        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
            } catch (IOException e) {
                Logging.warning("Problem creating language file '%s'", languageFileName);
                e.printStackTrace();
            }
        }
        if (languageFile.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(languageFile);
                language.load(reader);
            } catch (IOException e) {
                Logging.warning("Problem loading language file '%s'", languageFileName);
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignore) { }
                }
            }
        }
        for (final Object key : Messages.messages.keySet()) {
            if (!language.containsKey(key)) {
                language.put(key, Messages.messages.getProperty(key.toString()));
            }
        }
        return language;
    }

    private void storeProperties(@NotNull final String languageFileName, @NotNull final Properties properties) {
        final File languageFile = new File(dataFolder, languageFileName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(languageFile);
            properties.store(writer,
                    "You may insert color into the strings by preceding the color code with &."
                    + "\nExample: &cThis is red\n\nAny place where there is %s represents data"
                    + " to be filled in by the plugin.\nMAKE SURE THESE REMAIN IN ANY REPLACEMENTS!");
        } catch (IOException e) {
            Logging.warning("Problem saving language file '%s'", languageFileName);
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignore) { }
            }
        }
    }

    /**
     * Formats a string by replacing ampersand with the Section symbol and %s with the corresponding args
     * object in a fashion similar to String.format().
     *
     * @param string String to format.
     * @param args   Arguments to pass in via %n.
     * @return The formatted string.
     */
    @NotNull
    private static String format(@NotNull final Locale locale, @NotNull String string, final Object... args) {
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

    @Override
    @NotNull
    public String getMessage(@NotNull Message key, Object... args) {
        return format(locale, _getMessage(key), args);
    }

    @Override
    @NotNull
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(@NotNull final Locale locale) {
        this.locale = locale;
    }

    @Override
    public void loadLanguageFile(@NotNull final String languageFileName) {
        this.languageFileName = languageFileName;
        messages = getProperties(languageFileName);
        storeProperties(languageFileName, messages);
    }

    @Override
    public void pruneLanguageFile(){
        if (languageFileName != null) {
            final Properties language = getProperties(languageFileName);
            // Prune file
            final Iterator<Object> it = language.keySet().iterator();
            while (it.hasNext()) {
                final String key = it.next().toString();
                if (!Messages.messages.containsKey(key)) {
                    Logging.finer("Removing unused language: %s", key);
                    it.remove();
                }
            }
            storeProperties(languageFileName, language);
        }
    }
}
