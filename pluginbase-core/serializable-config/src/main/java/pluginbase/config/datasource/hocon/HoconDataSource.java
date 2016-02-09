package pluginbase.config.datasource.hocon;


import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.datasource.AbstractDataSource;
import pluginbase.config.serializers.SerializerSet;

public class HoconDataSource extends AbstractDataSource {

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Used to build a Hocon data source.
     *
     * @see AbstractDataSource.Builder
     */
    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();
        private final HoconConfigurationLoader.Builder defaultsBuilder = HoconConfigurationLoader.builder();

        protected Builder() { }

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
            return new HoconDataSource(builder.setSource(source).setSink(sink).build(),
                    defaultsSource != null ? defaultsBuilder.setSource(defaultsSource).setSink(defaultsSink).build() : null,
                    getBuiltSerializerSet(), commentsEnabled);
        }
    }

    private HoconDataSource(@NotNull HoconConfigurationLoader loader, @Nullable HoconConfigurationLoader defaultsLoader, @NotNull SerializerSet serializerSet, boolean commentsEnabled) {
        super(loader, defaultsLoader, serializerSet, commentsEnabled);
    }
}
