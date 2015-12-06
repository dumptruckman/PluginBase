package pluginbase.config.datasource.json;


import com.fasterxml.jackson.core.JsonFactory;
import ninja.leaping.configurate.json.FieldValueSeparatorStyle;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import pluginbase.config.datasource.AbstractDataSource;
import pluginbase.config.datasource.serializers.DoubleAsStringSerializer;
import pluginbase.config.datasource.serializers.LongAsStringSerializer;
import pluginbase.config.serializers.SerializerSet;

public class JsonDataSource extends AbstractDataSource {

    private static final SerializerSet DEFAULT_SERIALIZER_SET = SerializerSet.builder()
            .addSerializer(Long.class, new LongAsStringSerializer())
            .addSerializer(Double.class, new DoubleAsStringSerializer())
            .build();

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final JSONConfigurationLoader.Builder builder = JSONConfigurationLoader.builder();

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
            return new JsonDataSource(builder.setSource(source).setSink(sink).build(), getBuiltSerializerSet());
        }
    }

    private JsonDataSource(@NotNull JSONConfigurationLoader loader, @NotNull SerializerSet serializerSet) {
        super(loader, serializerSet);
    }
}
