package pluginbase.testplugin.pie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializableConfig;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

import java.util.ArrayList;
import java.util.List;

public class PieListSerializer implements Serializer<List<Pie>> {

    @Nullable
    @Override
    public Object serialize(@Nullable List<Pie> pieList, @NotNull SerializerSet serializerSet) {
        if (pieList == null) {
            return null;
        }
        List<Object> serialized = new ArrayList<Object>(pieList.size());
        for (Pie pie : pieList) {
            Object serializedPie = SerializableConfig.serialize(pie, serializerSet);
            serialized.add(serializedPie);
        }
        return serialized;
    }

    @Nullable
    @Override
    public List<Pie> deserialize(@Nullable Object serializedList, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serializedList == null || !(serializedList instanceof List)) {
            return null;
        }
        List<Pie> deserialized = new ArrayList<Pie>(((List) serializedList).size());
        for (Object serialized : (List) serializedList) {
            Pie pie = (Pie) SerializableConfig.deserializeAs(serialized, Pie.class, serializerSet);
            deserialized.add(pie);
        }
        return deserialized;
    }
}
