package pluginbase.config.datasource.yaml;


import org.jetbrains.annotations.NotNull;
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

        public Builder doComments(boolean doComments) {
            builder.doComments(doComments);
            return this;
        }

        @NotNull
        @Override
        public YamlDataSource build() {
            return new YamlDataSource(builder.setSource(source).setSink(sink).build(), getBuiltSerializerSet());
        }
    }

    private YamlDataSource(@NotNull YamlConfigLoader loader, @NotNull SerializerSet serializerSet) {
        super(loader, serializerSet);
    }
}
