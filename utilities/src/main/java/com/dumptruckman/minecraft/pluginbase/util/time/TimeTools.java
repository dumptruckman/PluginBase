package com.dumptruckman.minecraft.pluginbase.util.time;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

/**
 * Several utility methods for converting amounts of times from seconds to human readable string representations
 * and back.
 * <p/>
 * It is recommended that you generally use {@link Duration} for these purposes however.
 */
public class TimeTools {

    private TimeTools() {
        throw new AssertionError();
    }

    /**
     * Returns the amount of time the given amount of seconds represents as a short, concise human-readable String.
     * <p/>
     * Example: {@code 5d 1h 15m 32s}
     *
     * @param second the amount of seconds to stringify.
     * @return the amount of time the given amount of seconds represents as a short, concise human-readable String.
     */
    @NotNull
    public static String toShortForm(long second) {
        if (second == 0) {
            return "0s";
        }
        long minute = second / 60;
        second = second % 60;
        long hour = minute / 60;
        minute = minute % 60;
        long day = hour / 24;
        hour = hour % 24;
        StringBuilder time = new StringBuilder();
        if (day != 0) {
            time.append(hour).append("h ");
        }
        if (hour != 0) {
            time.append(hour).append("h ");
        }
        if (minute != 0) {
            time.append(minute).append("m ");
        }
        if (second != 0) {
            time.append(second).append("s");
        }
        return time.toString().trim();
    }

    /**
     * Returns the amount of time the given amount of seconds represents as a verbose human-readable String.
     * <p/>
     * Example: {@code 5 days 1 hour 15 minutes 32 seconds}
     *
     * @param second the amount of seconds to stringify.
     * @return the amount of time the given amount of seconds represents as a verbose human-readable String.
     */
    @NotNull
    public static String toLongForm(long second) {
        if (second == 0) {
            return "0 seconds";
        }
        long minute = second / 60;
        second = second % 60;
        long hour = minute / 60;
        minute = minute % 60;
        long day = hour / 24;
        hour = hour % 24;
        StringBuilder time = new StringBuilder();
        if (day != 0) {
            time.append(day);
        }
        if (day == 1) {
            time.append(" day ");
        } else if (day > 1) {
            time.append(" days ");
        }
        if (hour != 0) {
            time.append(hour);
        }
        if (hour == 1) {
            time.append(" hour ");
        } else if (hour > 1) {
            time.append(" hours ");
        }
        if (minute != 0) {
            time.append(minute);
        }
        if (minute == 1) {
            time.append(" minute ");
        } else if (minute > 1) {
            time.append(" minutes ");
        }
        if (second!= 0) {
            time.append(second);
        }
        if (second == 1) {
            time.append(" second");
        } else if (second > 1) {
            time.append(" seconds");
        }
        return time.toString().trim();
    }

    /**
     * Converts a string that represents a length of time into an amount of seconds.
     * <p/>
     * The string form must be:
     * <p/>
     * <b>Short form: </b>{@code DDd HHh MMm SSs}
     * <p/>
     * {@code DD} - an amount of days
     * <br/>
     * {@code HH} - an amount of hours
     * <br/>
     * {@code MM} - an amount of minutes
     * <br/>
     * {@code SS} - an amount of seconds
     * <p/>
     * Spaces in the string will not affect the parsing.
     * <br/>
     * In the short form, any of the time intervals may be omitted as long as one is present.
     * However, the order must remain consistent as given in the examples.  For instance, the minutes may not
     * come before the hours in the string.
     *
     * @param dhms the length of time in one of the allowed string forms.
     * @return an amount of seconds representing on the given length of time.
     * @throws ParseException in case the string is formatted incorrectly and the time cannot be interpreted from it.
     */
    public static long fromShortForm(@NotNull String dhms) throws ParseException {
        long seconds = 0, minutes = 0, hours = 0, days = 0;
        boolean valid = false;
        if (dhms.contains("d")) {
            try {
                days = Integer.parseInt(dhms.split("d")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("h") || dhms.contains("m") || dhms.contains("s")) {
                dhms = dhms.split("d")[1];
            }
        }
        if (dhms.contains("h")) {
            try {
                hours = Integer.parseInt(dhms.split("d")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("m") || dhms.contains("s")) {
                dhms = dhms.split("h")[1];
            }
        }
        if (dhms.contains("m")) {
            try {
                minutes = Integer.parseInt(dhms.split("m")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("s")) {
                dhms = dhms.split("m")[1];
            }
        }
        if (dhms.contains("s")) {
            try {
                seconds = Integer.parseInt(dhms.split("s")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
        }
        if (!valid) {
            throw new ParseException("'" + dhms + "' is not a valid duration format!", 0);
        }
        return (days * 86400) + (hours * 3600) + (minutes * 60) + seconds;
    }

    /**
     * Converts a string that represents a length of time into an amount of seconds.
     * <p/>
     * The string form must be:
     * <p/>
     * <b>Long form: </b>{@code DD day(s) HH hour(s) MM minute(s) SS second(s)}
     * <p/>
     * {@code DD} - an amount of days
     * <br/>
     * {@code HH} - an amount of hours
     * <br/>
     * {@code MM} - an amount of minutes
     * <br/>
     * {@code SS} - an amount of seconds
     * <br/>
     * {@code (s)} - this "s" is optional
     * <p/>
     * Spaces in the string will not affect the parsing.
     * <br/>
     * In the long form, any of the time intervals may be omitted as long as one is present.
     * However, the order must remain consistent as given in the examples.  For instance, the minutes may not
     * come before the hours in the string.
     *
     * @param dhms the length of time in one of the allowed string forms.
     * @return an amount of seconds representing on the given length of time.
     * @throws ParseException in case the string is formatted incorrectly and the time cannot be interpreted from it.
     */
    public static long fromLongForm(@NotNull String dhms) throws ParseException {
        long seconds = 0, minutes = 0, hours = 0, days = 0;
        boolean valid = false;
        if (dhms.contains("days")) {
            try {
                days = Integer.parseInt(dhms.split("days")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("hours") || dhms.contains("hour") || dhms.contains("minutes") || dhms.contains("seconds") || dhms.contains("minute") || dhms.contains("second")) {
                dhms = dhms.split("days")[1];
            }
        } else if (dhms.contains("day")) {
            try {
                days = Integer.parseInt(dhms.split("day")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("hours") || dhms.contains("hour") || dhms.contains("minutes") || dhms.contains("seconds") || dhms.contains("minute") || dhms.contains("second")) {
                dhms = dhms.split("day")[1];
            }
        }
        if (dhms.contains("hours")) {
            try {
                hours = Integer.parseInt(dhms.split("hours")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("minutes") || dhms.contains("seconds") || dhms.contains("minute") || dhms.contains("second")) {
                dhms = dhms.split("hours")[1];
            }
        } else if (dhms.contains("hour")) {
            try {
                hours = Integer.parseInt(dhms.split("hour")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("minutes") || dhms.contains("seconds") || dhms.contains("minute") || dhms.contains("second")) {
                dhms = dhms.split("hour")[1];
            }
        }
        if (dhms.contains("minutes")) {
            try {
                minutes = Integer.parseInt(dhms.split("minutes")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("seconds") || dhms.contains("second")) {
                dhms = dhms.split("minutes")[1];
            }
        } else if (dhms.contains("minute")) {
            try {
                minutes = Integer.parseInt(dhms.split("minute")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
            if (dhms.contains("seconds") || dhms.contains("second")) {
                dhms = dhms.split("minute")[1];
            }
        }
        if (dhms.contains("seconds")) {
            try {
                seconds = Integer.parseInt(dhms.split("seconds")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
        } else if (dhms.contains("second")) {
            try {
                seconds = Integer.parseInt(dhms.split("second")[0].replaceAll(" ", ""));
                valid = true;
            } catch (NumberFormatException ignore) { }
        }
        if (!valid) {
            throw new ParseException("'" + dhms + "' is not a valid duration format!", 0);
        }
        return (days * 86400) + (hours * 3600) + (minutes * 60) + seconds;
    }
}