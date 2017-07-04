/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.logging;

import org.jetbrains.annotations.NotNull;

import java.util.IllegalFormatException;

/**
 * Utility class for formatting strings similarly to {@link String#format(String, Object...)} but without exceptions.
 */
public class Formatter {

    /**
     * Functions exactly like {@link String#format(String, Object...)} but throws no exception on failure.
     * <br>
     * When there would have been an exception, it just returns the original message instead.
     *
     * @param message the string to format.
     * @param args the format arguments.
     * @return the formatted string or the original string if there was any exceptions during the format.
     */
    @NotNull
    public static String format(@NotNull final String message, @NotNull final Object... args) {
        try {
            return String.format(message, args);
        } catch (IllegalFormatException e) {
            final StringBuilder builder = new StringBuilder();
            for (final Object object : args) {
                if (builder.length() != 0) {
                    builder.append(", ");
                }
                builder.append(object);
            }
            System.out.println("Illegal format in the following message with args: " + builder.toString());
        }
        return message;
    }
}
