/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;


import org.jetbrains.annotations.Nullable;

/**
 * A class that represents the same thing as the java keyword <em>null</em>.
 * <p/>
 * This is particularly used for {@link NullProperty} but may server other purposes.
 * <p/>
 * Please do not try to instantiate this class.
 * Instead use this static {@link #NULL} for all purposes uses of this class.
 */
public final class Null {

    /**
     * A singleton instance of this Null class.
     */
    public static final Null NULL = new Null();

    private Null() { }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(@Nullable final Object o) {
        return o == null || (o instanceof Null && o == NULL);
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        return 0;
    }
}
