package pluginbase.plugin.metadata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.minecraft.location.BlockCoordinates;

/**
 * Implement in a class that represents block metadata.
 * <p/>
 * The implementing class must be compatible with serialization via {@link pluginbase.config.SerializableConfig}.
 * Each instance of this should only be added to a single {@link BlockMetadataRegistry}.
 */
public interface BlockMetadatable {

    /**
     * The block location that this metadata is for.
     *
     * @return The location of the metadata'd block.
     */
    @NotNull
    BlockCoordinates getLocation();

    /**
     * Sets the registry which this block metadata is stored in.
     * <p/>
     * Null may be given to disassociate this metadata from a registry.
     *
     * @param registry The block metadata registry to store this metadata in.
     */
    void setRegistry(@Nullable BlockMetadataRegistry registry);
}
