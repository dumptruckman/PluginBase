package pluginbase.config.datasource.hocon;


import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import pluginbase.config.datasource.AbstractDataSource;
import pluginbase.config.serializers.SerializerSet;

public class HoconDataSource extends AbstractDataSource {

    @NotNull
    public static Builder builder() {
        return new Builder(SerializerSet.defaultSet());
    }

    @NotNull
    public static Builder builder(@NotNull SerializerSet serializerSet) {
        return new Builder(serializerSet);
    }

    /**
     * Used to build a Hocon data source.
     *
     * @see AbstractDataSource.Builder
     */
    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();

        protected Builder(@NotNull SerializerSet serializerSet) {
            super(serializerSet);
        }

        @NotNull
        public ConfigRenderOptions getRenderOptions() {
            return builder.getRenderOptions();
        }

        @NotNull
        public ConfigParseOptions getParseOptions() {
            return builder.getParseOptions();
        }

        @NotNull
        public Builder setRenderOptions(@NotNull ConfigRenderOptions options) {
            builder.setRenderOptions(options);
            return this;
        }

        @NotNull
        public Builder setParseOptions(@NotNull ConfigParseOptions options) {
            builder.setParseOptions(options);
            return this;
        }

        @NotNull
        @Override
        public HoconDataSource build() {
            return new HoconDataSource(builder.setSource(source).setSink(sink).build(), serializerSet);
        }
    }

    private HoconDataSource(@NotNull HoconConfigurationLoader loader, @NotNull SerializerSet serializerSet) {
        super(loader, serializerSet);
    }
}
