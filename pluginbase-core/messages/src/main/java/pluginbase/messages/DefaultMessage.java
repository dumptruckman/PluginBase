package pluginbase.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DefaultMessage implements Message {

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
                    } catch (SAXException | IOException e) {
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
    @Nullable
    private final String[] key;
    private final int argCount;

    DefaultMessage(@Nullable final String key, @NotNull final String def) {
        if (key != null) {
            this.key = key.split("\\.");
        } else {
            this.key = null;
        }
        this.def = Theme.parseMessage(def);
        this.argCount = countArgs(this.def);
    }

    DefaultMessage(@NotNull final String def) {
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
    @Nullable
    public Object[] getKey() {
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
