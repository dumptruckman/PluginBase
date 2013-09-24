package com.dumptruckman.minecraft.pluginbase.config.examples;

import com.dumptruckman.minecraft.pluginbase.config.properties.PropertyAliases;

public class Parent {

    static {
        PropertyAliases.createAlias(Parent.class, "cbool", "aChild", "aBoolean");
    }

    private Child aChild;

    private Parent() { }

    public Parent(Child aChild) {
        this.aChild = aChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parent)) return false;

        final Parent parent = (Parent) o;

        if (!aChild.equals(parent.aChild)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return aChild.hashCode();
    }

    @Override
    public String toString() {
        return "Parent{" +
                "aChild=" + aChild +
                '}';
    }
}
