package pluginbase.testplugin.pie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;

public class PieSerializer implements Serializer<Pie> {

    @Nullable
    @Override
    public Object serialize(@Nullable Pie pie) {
        if (pie == null) {
            return null;
        }
        Object serializedPie = Serializers.getDefaultSerializer().serialize(pie.properties);
        return serializedPie;
    }

    @Nullable
    @Override
    public Pie deserialize(@Nullable Object serialized, @NotNull Class wantedType) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        PieProperties pieProperties = (PieProperties) Serializers.getDefaultSerializer().deserialize(serialized, PieProperties.class);
        return new Pie(pieProperties);
    }
}
