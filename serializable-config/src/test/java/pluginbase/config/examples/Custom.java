package pluginbase.config.examples;

import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.CustomSerializer;

@SerializeWith(CustomSerializer.class)
public class Custom {

    public String name;

    public Custom(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Custom)) return false;

        final Custom custom = (Custom) o;

        if (!name.equals(custom.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Custom{" +
                "name='" + name + '\'' +
                '}';
    }
}
