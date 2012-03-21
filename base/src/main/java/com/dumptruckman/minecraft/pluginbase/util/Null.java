package com.dumptruckman.minecraft.pluginbase.util;

public final class Null {

    public static final Null NULL = new Null();

    private Null() {
        throw new AssertionError();
    }

    public boolean equals(Object o) {
        return o == null;
    }

    public int hashCode() {
        return 0;
    }
}
