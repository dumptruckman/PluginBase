package pluginbase.util.time;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

/**
 * A class that interprets and translates a length of time.
 */
public final class Duration {

    /**
     * Creates a Duration object based on a length of time interpreted from a string form.
     * <br>
     * The string form can fit one of the following formats.
     * <br>
     * <b>Short form: </b>{@code DDd HHh MMm SSs}
     * <br/>
     * <b>Long form: </b>{@code DD day(s) HH hour(s) MM minute(s) SS second(s)}
     * <br/>
     * <b>Seconds: </b>{@code SS}
     * <br>
     * {@code DD} - an amount of days
     * <br/>
     * {@code HH} - an amount of hours
     * <br/>
     * {@code MM} - an amount of minutes
     * <br/>
     * {@code SS} - an amount of seconds
     * <br/>
     * {@code (s)} - this "s" is optional
     * <br>
     * Spaces in the string will not affect the parsing.
     * <br/>
     * In the short and long form, any of the time intervals may be omitted as long as one is present.
     * However, the order must remain consistent as given in the examples.  For instance, the minutes may not
     * come before the hours in the string.
     *
     * @param stringForm the length of time in one of the allowed string forms.
     * @return a Duration object based on the given length of time.
     * @throws ParseException in case the string is formatted incorrectly and the time cannot be interpreted from it.
     */
    @NotNull
    public static Duration valueOf(@NotNull final String stringForm) throws ParseException {
        long time = -1;
        try {
            time = TimeTools.fromShortForm(stringForm);
        } catch (ParseException ignore) { }
        if (time == -1) {
            try {
                time = Long.valueOf(stringForm);
            } catch (NumberFormatException ignore) { }
        }
        if (time == -1) {
            try {
                time = TimeTools.fromLongForm(stringForm);
            } catch (ParseException ignore) { }
        }
        if (time < 0) {
            throw new ParseException("Could not interpret the length of time from the given string.", 0);
        }
        return new Duration(time * 1000L);
    }

    /**
     * Returns a new Duration object based on a given number of milliseconds.
     *
     * @param duration The length of the duration in milliseconds.
     * @return a new Duration based on the given number of milliseconds.
     */
    @NotNull
    public static Duration valueOf(final long duration) {
        return new Duration(duration);
    }

    /**
     * Returns a new Duration object based on a given number of seconds.
     *
     * @param duration The length of the duration in seconds.
     * @return a new Duration based on the given number of seconds.
     */
    @NotNull
    public static Duration fromSeconds(final long duration) {
        return new Duration(duration * 1000);
    }

    private final long duration;
    private final String shortForm, longForm;

    private Duration(final long duration) {
        this.duration = duration;
        final long seconds = duration / 1000L;
        this.longForm = TimeTools.toLongForm(seconds);
        this.shortForm = TimeTools.toShortForm(seconds);
    }

    /**
     * Returns the amount of time this Duration object represents in seconds.
     *
     * @return the amount of time this Duration object represents in seconds.
     */
    public long asSeconds() {
        return duration / 1000L;
    }

    /**
     * Returns the amount of time this Duration object represents in milliseconds.
     *
     * @return the amount of time this Duration object represents in milliseconds.
     */
    public long asMilliseconds() {
        return duration;
    }

    /**
     * Returns the amount of time this Duration object represents as a verbose human-readable String.
     * <br>
     * Example: {@code 5 days 1 hour 15 minutes 32 seconds}
     *
     * @return the amount of time this Duration object represents as a verbose human-readable String.
     */
    @NotNull
    public String asVerboseString() {
        return longForm;
    }

    /**
     * Returns the amount of time this Duration object represents as a short, concise human-readable String.
     * <br>
     * Example: {@code 5d 1h 15m 32s}
     *
     * @return the amount of time this Duration object represents as a verbose human-readable String.
     */
    @NotNull
    public String asSimpleString() {
        return shortForm;
    }

    /**
     * Returns the amount of time this Duration object represents as a short, concise human-readable String.
     * <br>
     * Example: {@code 5d 1h 15m 32s}
     *
     * @return the amount of time this Duration object represents as a verbose human-readable String.
     */
    @NotNull
    @Override
    public String toString() {
        return asSimpleString();
    }
}
