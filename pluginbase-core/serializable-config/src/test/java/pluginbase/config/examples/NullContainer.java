package pluginbase.config.examples;

public class NullContainer {

    public Parent parent = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NullContainer)) return false;

        final NullContainer that = (NullContainer) o;

        return !(parent != null ? !parent.equals(that.parent) : that.parent != null);

    }

    @Override
    public int hashCode() {
        return parent != null ? parent.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NullContainer{" +
                "parent=" + parent +
                '}';
    }
}
