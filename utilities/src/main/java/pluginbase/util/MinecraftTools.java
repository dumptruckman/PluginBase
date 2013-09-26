/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.util;

/**
 * General utility to help with minecraftian things.
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
    public static long secondsToTicks(long seconds) {
        return seconds * TICKS_PER_SECOND;
    }
}
