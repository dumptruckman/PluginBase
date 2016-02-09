package pluginbase.config.datasource.yaml;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import pluginbase.config.datasource.AbstractDataSource;
import pluginbase.config.serializers.SerializerSet;

public class YamlDataSource extends AbstractDataSource {

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final YamlConfigLoader.Builder builder = YamlConfigLoader.builder();
        private final YamlConfigLoader.Builder defaultsBuilder = YamlConfigLoader.builder();

        protected Builder() { }

        public Builder setIndent(int indent) {
            builder.setIndent(indent);
            return this;
        }

        /**
         * Sets the flow style for this configuration
         * Flow: the compact, json-like representation.<br>
         * Example: <code>
         *     {value: [list, of, elements], another: value}
         * </code>
         *
         * Block: expanded, traditional YAML<br>
         * Emample: <code>
         *     value:
         *     - list
         *     - of
         *     - elements
         *     another: value
         * </code>
         *
         * @param style the appropriate flow style to use
         * @return this
         */
        public Builder setFlowStyle(DumperOptions.FlowStyle style) {
            builder.setFlowStyle(style);
            return this;
        }

        @Override
        public Builder setCommentsEnabled(boolean commentsEnabled) {
            super.setCommentsEnabled(commentsEnabled);
            builder.setCommentsEnabled(commentsEnabled);
            return this;
        }

        @NotNull
        @Override
        public YamlDataSource build() {
            return new YamlDataSource(builder.setSource(source).setSink(sink).build(),
                    defaultsSource != null ? defaultsBuilder.setSource(defaultsSource).setSink(defaultsSink).build() : null
                    , getBuiltSerializerSet(), commentsEnabled);
        }
    }

    private YamlDataSource(@NotNull YamlConfigLoader loader, @Nullable YamlConfigLoader defaultsLoader, @NotNull SerializerSet serializerSet, boolean commentsEnabled) {
        super(loader, defaultsLoader, serializerSet, commentsEnabled);
    }
}
