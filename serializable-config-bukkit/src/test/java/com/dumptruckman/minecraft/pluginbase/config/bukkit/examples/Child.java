package com.dumptruckman.minecraft.pluginbase.config.bukkit.examples;

import com.dumptruckman.minecraft.pluginbase.config.annotation.Comment;

public final class Child {

    @Comment("# Test boolean comments")
    private boolean aBoolean;

    private Child() { }

    public Child(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public boolean equals(Object o) {
        return o instanceof Child && ((Child) o).aBoolean == this.aBoolean;
    }
}
