package com.dumptruckman.minecraft.pluginbase.util;

/**
 * General minecraft to help with minecraftian things.
 */
public class MinecraftTools {

    private static final int TICKS_PER_SECOND = 20;

    protected MinecraftTools() {
        throw new AssertionError();
    }

    /**
     * Converts an amount of seconds to the appropriate amount of ticks.
     *
     * @param seconds Amount of seconds to convertForSet
     * @return Ticks converted from seconds.
     */
    public static long convertSecondsToTicks(long seconds) {
        return seconds * TICKS_PER_SECOND;
    }
}
