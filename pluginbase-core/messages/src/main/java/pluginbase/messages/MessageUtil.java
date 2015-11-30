package pluginbase.messages;

import org.jetbrains.annotations.NotNull;

import java.util.IllegalFormatException;
import java.util.Locale;

public class MessageUtil {

    private MessageUtil() {
        throw new AssertionError();
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
    public static String formatMessage(@NotNull final Locale locale, @NotNull String string, @NotNull final Object... args) throws IllegalFormatException {
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
        string = String.format(locale, string, args);
        return string;
    }
}
