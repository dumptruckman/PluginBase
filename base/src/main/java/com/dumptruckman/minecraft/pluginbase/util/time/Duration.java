package com.dumptruckman.minecraft.pluginbase.util.time;

public final class Duration {

    public static Duration valueOf(String shortForm) {
        return new Duration(shortForm);
    }

    public static Duration valueOf(long duration) {
        return new Duration(duration);
    }

    private final long duration;
    private final String shortForm, longForm;

    private Duration(String shortForm) {
        this.shortForm = shortForm;
        this.duration = TimeTools.fromShortForm(shortForm);
        this.longForm = TimeTools.toLongForm(duration);
    }

    private Duration(long duration) {
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
