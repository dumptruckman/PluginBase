package pluginbase.config.datasource.gson;


import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import pluginbase.config.datasource.AbstractDataSource;
import pluginbase.config.serializers.SerializerSet;

public class GsonDataSource extends AbstractDataSource {

    @NotNull
    public static Builder builder() {
        return new Builder(SerializerSet.defaultSet());
    }

    @NotNull
    public static Builder builder(@NotNull SerializerSet serializerSet) {
        return new Builder(serializerSet);
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final GsonConfigurationLoader.Builder builder = GsonConfigurationLoader.builder();

        protected Builder(@NotNull SerializerSet serializerSet) {
            super(serializerSet);
        }

        public Builder setLenient(boolean lenient) {
            builder.setLenient(lenient);
            return this;
        }

        @NotNull
        @Override
        public GsonDataSource build() {
            return new GsonDataSource(builder.setSource(source).setSink(sink).build(), serializerSet);
        }
    }

    private GsonDataSource(@NotNull GsonConfigurationLoader loader, @NotNull SerializerSet serializerSet) {
        super(loader, serializerSet);
    }
}
