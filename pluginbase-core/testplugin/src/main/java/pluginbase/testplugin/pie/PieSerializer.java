package pluginbase.testplugin.pie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializableConfig;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public class PieSerializer implements Serializer<Pie> {

    @Nullable
    @Override
    public Object serialize(@Nullable Pie pie, @NotNull SerializerSet serializerSet) {
        if (pie == null) {
            return null;
        }
        Object serializedPie = SerializableConfig.serialize(pie.properties, serializerSet);
        return serializedPie;
    }

    @Nullable
    @Override
    public Pie deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        PieProperties pieProperties = SerializableConfig.deserializeAs(serialized, PieProperties.class, serializerSet);
        return new Pie(pieProperties);
    }
}
