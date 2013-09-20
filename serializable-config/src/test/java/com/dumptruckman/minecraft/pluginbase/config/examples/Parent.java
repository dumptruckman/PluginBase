package com.dumptruckman.minecraft.pluginbase.config.examples;

public class Parent {

    private Child aChild;

    private Parent() { }

    public Parent(Child aChild) {
        this.aChild = aChild;
    }

    public boolean equals(Object o) {
        return o instanceof Parent && ((Parent) o).aChild.equals(this.aChild);
    }
}
