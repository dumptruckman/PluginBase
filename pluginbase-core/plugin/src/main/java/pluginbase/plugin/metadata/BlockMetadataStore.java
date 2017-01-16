package pluginbase.plugin.metadata;

import org.jetbrains.annotations.NotNull;
import pluginbase.config.annotation.NoTypeKey;
import pluginbase.minecraft.location.BlockCoordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a data class for storage of block metadata in a {@link pluginbase.config.SerializableConfig}.
 * <p/>
 * The data is stored as a list of objects where each object keeps track of location data itself. The data is then
 * loaded as a list and piped out as a map with each object mapped to its coordinates.
 *
 * @param <DataType> This is the type of data to be stored which must serializable via
 * {@link pluginbase.config.SerializableConfig}.
 */
@NoTypeKey
class BlockMetadataStore<DataType extends BlockMetadatable> {

    List<DataType> metadata = new ArrayList<>();

    @NotNull
    Map<BlockCoordinates, DataType> loadData(@NotNull BlockMetadataRegistry registry) {
        Map<BlockCoordinates, DataType> map = new HashMap<>(metadata.size());
        for (DataType d : metadata) {
            d.setRegistry(registry);
            map.put(d.getLocation(), d);
        }
        return map;
    }

    BlockMetadataStore<DataType> saveData(@NotNull Map<BlockCoordinates, DataType> data) {
        metadata = new ArrayList<>(data.values());
        return this;
    }
}
