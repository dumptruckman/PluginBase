package com.dumptruckman.minecraft.pluginbase.config.examples;

import com.dumptruckman.minecraft.pluginbase.config.PropertyAliases;

public class Parent {

    static {
        PropertyAliases.createAlias(Parent.class, "cbool", "aChild", "aBoolean");
    }

    private Child aChild;

    private Parent() { }

    public Parent(Child aChild) {
        this.aChild = aChild;
    }

    public boolean equals(Object o) {
        return o instanceof Parent && ((Parent) o).aChild.equals(this.aChild);
    }
}
