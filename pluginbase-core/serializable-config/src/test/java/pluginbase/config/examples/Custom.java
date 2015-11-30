package pluginbase.config.examples;

import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.CustomSerializer;

import java.util.Arrays;

@SerializeWith(CustomSerializer.class)
public class Custom {

    public String name;
    public Data data = new Data();

    public Custom(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Custom)) return false;

        final Custom custom = (Custom) o;

        if (!name.equals(custom.name)) return false;
        return data.equals(custom.data);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {

        public Object[] array = new Object[] {1, 2, 3};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Data)) return false;

            final Data data = (Data) o;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(array, data.array);

        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }

        @Override
        public String toString() {
            return "Data{" +
                    "array=" + Arrays.toString(array) +
                    '}';
        }
    }
}
