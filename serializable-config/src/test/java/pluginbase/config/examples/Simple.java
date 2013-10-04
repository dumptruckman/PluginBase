package pluginbase.config.examples;

public class Simple {

    public static Simple valueOf(String string) {
        Simple simple = new Simple();
        simple.string = string;
        return simple;
    }

    public String string = "string";

    public Simple() { }

    public Simple(String string) {
        this.string = string;
    }

    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Simple)) return false;

        final Simple simple = (Simple) o;

        if (!string.equals(simple.string)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}
