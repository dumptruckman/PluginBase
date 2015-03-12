/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages;

import pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A localization key and its defaults.
 * <p/>
 * The key represents the location of the localized strings in a language file.
 * <br/>
 * The default is what should populate the localization file by default.
 */
public final class Message {

    static {
        // Load the theme from theme.xml
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            Enumeration<URL> urls = Messages.class.getClassLoader().getResources(Theme.getThemeResource());
            if (urls.hasMoreElements()) {
                try {
                    DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
                    try {
                        Theme.loadTheme(documentBuilder.parse(urls.nextElement().openStream()));
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private final String def;

    private final String key;

    private final int argCount;

    private Message(@NotNull final String key, @NotNull final String def) {
        this.key = key;
        this.def = Theme.parseMessage(def);
        this.argCount = countArgs(this.def);
    }

    Message(@NotNull final String def) {
        this.key = null;
        this.def = Theme.parseMessage(def);
        this.argCount = countArgs(this.def);
    }

    private static final Pattern PATTERN = Pattern.compile("%s");

    static int countArgs(@NotNull final String def) {
        final Matcher matcher = PATTERN.matcher(def);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

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
     */
    public static Message createMessage(@NotNull final String key, @NotNull final String def) {
        return new Message(key, def);
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
    public static BundledMessage bundleMessage(@NotNull final Message message, @NotNull final Object... args) {
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
    public String getDefault() {
        return def;
    }

    /**
     * The localization key for the message.
     *
     * @return The localization key for the message.
     */
    @NotNull
    public String getKey() {
        return key;
    }

    /**
     * Gets the number of expected arguments for this message.
     * <p/>
     * This is used to validate localized versions of this message to ensure they were given the appropriate
     * amount of arguments.
     *
     * @return the number of expected arguments for this message.
     */
    public int getArgCount() {
        return argCount;
    }

    @Override
    public String toString() {
        return "Message{" +
                "def='" + def + '\'' +
                ", key='" + key + '\'' +
                ", argCount=" + argCount +
                '}';
    }
}
