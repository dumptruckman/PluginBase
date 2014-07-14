package pluginbase.bukkit.pie;

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
        return Serializers.getDefaultSerializer().serialize(pie.properties);
    }

    @Nullable
    @Override
    public Pie deserialize(@Nullable Object serialized, @NotNull Class wantedType) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        return new Pie((PieProperties) Serializers.getDefaultSerializer().deserialize(serialized, PieProperties.class));
    }
}
