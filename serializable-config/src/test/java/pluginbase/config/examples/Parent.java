package pluginbase.config.examples;

import pluginbase.config.annotation.Comment;
import pluginbase.config.properties.PropertyAliases;

public class Parent {

    static {
        PropertyAliases.createAlias(Parent.class, "cbool", "aChild", "aBoolean");
    }

    @Comment({"# Test comment 1", "Test comment 2"})
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
