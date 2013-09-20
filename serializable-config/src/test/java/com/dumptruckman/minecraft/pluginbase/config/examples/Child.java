package com.dumptruckman.minecraft.pluginbase.config.examples;

public class Child {

    private boolean aBoolean;

    private Child() { }

    public Child(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public boolean equals(Object o) {
        return o instanceof Child && ((Child) o).aBoolean == this.aBoolean;
    }
}
