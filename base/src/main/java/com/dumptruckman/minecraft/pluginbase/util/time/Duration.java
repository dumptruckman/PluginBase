package com.dumptruckman.minecraft.pluginbase.util.time;

import java.text.ParseException;

public final class Duration {

    public static Duration valueOf(final String shortForm) {
        long time = -1;
        try {
            time = TimeTools.fromShortForm(shortForm);
        } catch (ParseException ignore) { }
        if (time == -1) {
            try {
                time = Long.valueOf(shortForm);
            } catch (NumberFormatException ignore) { }
        }
        if (time == -1) {
            try {
                time = TimeTools.fromLongForm(shortForm);
            } catch (ParseException ignore) { }
        }
        if (time < 0) {
            time = 0;
        }
        return new Duration(time * 1000L);
    }

    /**
     * Returns a new Duration object based on a given number of milliseconds.
     *
     * @param duration The length of the duration in milliseconds.
     * @return a new Duration based on the given number of milliseconds.
     */
    public static Duration valueOf(final long duration) {
        return new Duration(duration);
    }

    /**
     * Returns a new Duration object based on a given number of seconds.
     *
     * @param duration The length of the duration in seconds.
     * @return a new Duration based on the given number of seconds.
     */
    public static Duration fromSeconds(final long duration) {
        return new Duration(duration);
    }

    private final long duration;
    private final String shortForm, longForm;

    private Duration(final long duration) {
        this.duration = duration;
        final long seconds = duration / 1000L;
        this.longForm = TimeTools.toLongForm(seconds);
        this.shortForm = TimeTools.toShortForm(seconds);
    }

    public long asSeconds() {
        return duration / 1000L;
    }

    public long asMilliseconds() {
        return duration;
    }

    public String asPrettyString() {
        return longForm;
    }

    @Override
    public String toString() {
        return shortForm;
    }
}
