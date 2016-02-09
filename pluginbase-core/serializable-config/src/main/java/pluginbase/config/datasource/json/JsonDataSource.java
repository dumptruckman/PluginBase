package pluginbase.config.datasource.json;


import com.fasterxml.jackson.core.JsonFactory;
import ninja.leaping.configurate.json.FieldValueSeparatorStyle;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.datasource.AbstractDataSource;
import pluginbase.config.datasource.serializers.DoubleAsStringSerializer;
import pluginbase.config.datasource.serializers.LongAsStringSerializer;
import pluginbase.config.serializers.SerializerSet;

public class JsonDataSource extends AbstractDataSource {

    private static final LongAsStringSerializer LONG_AS_STRING_SERIALIZER = new LongAsStringSerializer();
    private static final DoubleAsStringSerializer DOUBLE_AS_STRING_SERIALIZER  = new DoubleAsStringSerializer();
    private static final SerializerSet DEFAULT_SERIALIZER_SET = SerializerSet.builder()
            .addSerializer(Long.class, () -> LONG_AS_STRING_SERIALIZER)
            .addSerializer(Double.class, () -> DOUBLE_AS_STRING_SERIALIZER)
            .build();

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final JSONConfigurationLoader.Builder builder = JSONConfigurationLoader.builder();
        private final JSONConfigurationLoader.Builder defaultsBuilder = JSONConfigurationLoader.builder();

        protected Builder() { }

        public JsonFactory getFactory() {
            return this.builder.getFactory();
        }

        public Builder setIndent(int indent) {
            builder.setIndent(indent);
            return this;
        }

        public Builder setFieldValueSeparatorStyle(FieldValueSeparatorStyle style) {
            builder.setFieldValueSeparatorStyle(style);
            return this;
        }

        @Override
        protected SerializerSet getDataSourceDefaultSerializerSet() {
            return DEFAULT_SERIALIZER_SET;
        }

        @NotNull
        @Override
        public JsonDataSource build() {
            return new JsonDataSource(builder.setSource(source).setSink(sink).build(),
                    defaultsSource != null ? defaultsBuilder.setSource(defaultsSource).setSink(defaultsSink).build() : null,
                    getBuiltSerializerSet());
        }
    }

    private JsonDataSource(@NotNull JSONConfigurationLoader loader, @Nullable JSONConfigurationLoader defaultsLoader, @NotNull SerializerSet serializerSet) {
        super(loader, defaultsLoader, serializerSet, false);
    }
}
