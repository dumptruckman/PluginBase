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
        return new Duration(time);
    }

    public static Duration valueOf(final long duration) {
        return new Duration(duration);
    }

    private final long duration;
    private final String shortForm, longForm;

    private Duration(final long duration) {
        this.duration = duration;
        this.longForm = TimeTools.toLongForm(duration);
        this.shortForm = TimeTools.toShortForm(duration);
    }

    public long toLong() {
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
