package com.dumptruckman.minecraft.pluginbase.config.examples;

import com.dumptruckman.minecraft.pluginbase.config.annotation.Comment;

public class Child {

    @Comment("# Test boolean comments")
    private boolean aBoolean;

    private Child() { }

    public Child(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Child)) return false;

        final Child child = (Child) o;

        if (aBoolean != child.aBoolean) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (aBoolean ? 1 : 0);
    }

    @Override
    public String toString() {
        return "Child{" +
                "aBoolean=" + aBoolean +
                '}';
    }
}
