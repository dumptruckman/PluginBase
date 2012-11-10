/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

public final class Null {

    public static final Null NULL = new Null();

    private Null() {
        throw new AssertionError();
    }

    public boolean equals(Object o) {
        return o == NULL || o == null;
    }

    public int hashCode() {
        return 0;
    }
}
