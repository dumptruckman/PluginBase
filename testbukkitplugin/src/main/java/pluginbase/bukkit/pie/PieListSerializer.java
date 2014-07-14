package pluginbase.bukkit.pie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;

import java.util.ArrayList;
import java.util.List;

public class PieListSerializer implements Serializer<List<Pie>> {

    @Nullable
    @Override
    public Object serialize(@Nullable List<Pie> pieList) {
        if (pieList == null) {
            return null;
        }
        List<Object> serialized = new ArrayList<Object>(pieList.size());
        for (Pie pie : pieList) {
            serialized.add(Serializers.getDefaultSerializer().serialize(pie));
        }
        return serialized;
    }

    @Nullable
    @Override
    public List<Pie> deserialize(@Nullable Object serializedList, @NotNull Class wantedType) throws IllegalArgumentException {
        if (serializedList == null || !(serializedList instanceof List)) {
            return null;
        }
        List<Pie> deserialized = new ArrayList<Pie>(((List) serializedList).size());
        for (Object serialized : (List) serializedList) {
            deserialized.add((Pie) Serializers.getDefaultSerializer().deserialize(serialized, Pie.class));
        }
        return deserialized;
    }
}
