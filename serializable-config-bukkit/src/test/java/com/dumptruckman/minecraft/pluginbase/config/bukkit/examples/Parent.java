package com.dumptruckman.minecraft.pluginbase.config.bukkit.examples;

import com.dumptruckman.minecraft.pluginbase.config.annotation.Comment;
import com.dumptruckman.minecraft.pluginbase.config.properties.PropertyAliases;

public final class Parent {

    static {
        PropertyAliases.createAlias(Parent.class, "cbool", "aChild", "aBoolean");
    }

    @Comment({"# Test comment 1", "Test comment 2"})
    private Child aChild;

    private Parent() { }

    public Parent(Child aChild) {
        this.aChild = aChild;
    }

    public boolean equals(Object o) {
        return o instanceof Parent && ((Parent) o).aChild.equals(this.aChild);
    }

    @Override
    public String toString() {
        return "Parent{" +
                "aChild=" + aChild +
                '}';
    }
}
