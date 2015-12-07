package pluginbase.config.datasource.gson;


import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import pluginbase.config.datasource.AbstractDataSource;
import pluginbase.config.datasource.serializers.LongAsStringSerializer;
import pluginbase.config.serializers.SerializerSet;

public class GsonDataSource extends AbstractDataSource {

    private static final SerializerSet DEFAULT_SERIALIZER_SET = SerializerSet.builder().addSerializer(Long.class, new LongAsStringSerializer()).build();

    /**
     * Returns the default serializer set used for a Gson data source.
     * <p/>
     * Long values are given special treatment in this set.
     *
     * @return the default serializer set used for a Gson data source.
     */
    public static SerializerSet defaultSerializerSet() {
        return DEFAULT_SERIALIZER_SET;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final GsonConfigurationLoader.Builder builder = GsonConfigurationLoader.builder();

        protected Builder() { }

        public Builder setLenient(boolean lenient) {
            builder.setLenient(lenient);
            return this;
        }

        @Override
        protected SerializerSet getDataSourceDefaultSerializerSet() {
            return DEFAULT_SERIALIZER_SET;
        }

        @NotNull
        @Override
        public GsonDataSource build() {
            return new GsonDataSource(builder.setSource(source).setSink(sink).build(), getBuiltSerializerSet());
        }
    }

    private GsonDataSource(@NotNull GsonConfigurationLoader loader, @NotNull SerializerSet serializerSet) {
        super(loader, serializerSet, false);
    }
}
