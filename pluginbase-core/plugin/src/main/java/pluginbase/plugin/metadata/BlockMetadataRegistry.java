package pluginbase.plugin.metadata;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.datasource.DataSource;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.location.BlockCoordinates;
import pluginbase.plugin.PluginBase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Acts as a go-between for block metadata and a storage medium.
 * <br>
 * This class uses a single worker thread to handle loading and saving, however, loading will always block the main
 * thread.
 *
 * @param <DataType> The type of metadata to handle.
 */
public class BlockMetadataRegistry<DataType extends BlockMetadatable> {

    /**
     * Creates a builder object for a BlockMetadataRegistry.
     *
     * @param plugin The plugin owning the metadata.
     * @param dataSource The data source for storage of the metadata.
     * @return A builder object for a BlockMetadataRegistry.
     */
    public static Builder builder(@NotNull PluginBase plugin, @NotNull DataSource dataSource) {
        return new Builder(plugin, dataSource);
    }

    /**
     * A builder class for a BlockMetadataRegistry.
     *
     * @param <DataType> The type of metadata to handle.
     */
    public static class Builder<DataType extends BlockMetadatable> {

        @NotNull
        private final PluginBase plugin;
        @NotNull
        private final DataSource dataSource;
        private Consumer<Map<BlockCoordinates, DataType>> cleaner;

        private Builder(@NotNull PluginBase plugin, @NotNull DataSource dataSource) {
            this.plugin = plugin;
            this.dataSource = dataSource;
        }

        /**
         * Specifies a consumer that will handle clean up of the metadata whenever it is loaded from the datasource
         * via {@link #loadMetadata()}.
         *
         * @param cleaner The cleanup consumer to use.
         * @return This builder object for method chaining.
         */
        @NotNull
        public Builder setMetaDataCleaner(@NotNull Consumer<Map<BlockCoordinates, DataType>> cleaner) {
            this.cleaner = cleaner;
            return this;
        }

        /**
         * Builds the BlockMetadataRegistry.
         *
         * @return A BlockMetadataRegistry with all the given options.
         */
        @NotNull
        public BlockMetadataRegistry<DataType> build() {
            return new BlockMetadataRegistry<DataType>(plugin, dataSource, cleaner);
        }
    }

    @NotNull
    private final PluginBase plugin;
    @NotNull
    private final DataSource dataSource;
    @Nullable
    private final Consumer<Map<BlockCoordinates, DataType>> cleaner;

    private Map<BlockCoordinates, DataType> blockMetadata = new ConcurrentHashMap<>();
    private final ExecutorService dataWorker = Executors.newSingleThreadExecutor();

    private int saveTaskId;

    private BlockMetadataRegistry(@NotNull PluginBase plugin, @NotNull DataSource dataSource,
                                 @Nullable Consumer<Map<BlockCoordinates, DataType>> cleaner) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.cleaner = cleaner;
    }

    /**
     * Loads metadata from this registry's data source.
     * <br>
     * The loading occurs on a single worker thread belonging to this registry but blocks the main thread until
     * finished loading. Cleanup, if a cleaner was specified, will occur on the main thread after loading.
     */
    public void loadMetadata() {
        Future<Map<BlockCoordinates, DataType>> lockLoader = dataWorker.submit(() -> {
            try {
                BlockMetadataStore dataStore = dataSource.load(BlockMetadataStore.class);
                if (dataStore == null) {
                    plugin.getLog().fine("Could not load block metadata source... creating new data.");
                    dataStore = new BlockMetadataStore();
                    dataSource.save(dataStore);
                }
                return dataStore.loadData(BlockMetadataRegistry.this);
            } catch (SendablePluginBaseException e) {
                e.printStackTrace();
                plugin.getLog().severe("Unable to load block metadata");
                return new HashMap<>();
            }
        });
        try {
            blockMetadata = lockLoader.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (cleaner != null) {
            cleaner.accept(blockMetadata);
        }
        plugin.getLog().fine("Loaded metadata for " + blockMetadata.size() + " blocks");
    }

    /**
     * Saves all metadata stored in this registry to the registry's data source.
     * <br>
     * This save operation is performed on this registry's single worker thread and can optionally block the main
     * thread.
     *
     * @param blockMainThread True will cause this operation to block the main thread until complete.
     */
    public void saveMetaData(boolean blockMainThread) {
        Future lockSaver = dataWorker.submit(() -> {
            try {
                dataSource.save(new BlockMetadataStore<DataType>().saveData(blockMetadata));
            } catch (SendablePluginBaseException e) {
                e.printStackTrace();
                plugin.getLog().severe("Could not save block metadata!");
            }
        });
        if (blockMainThread) {
            try {
                lockSaver.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Schedules an automatic, non-blocking save to occur after the given number of ticks and repeating until stopped.
     *
     * @param saveTicks the number of ticks before the first save and between each save thereafter.
     */
    public void scheduleSave(long saveTicks) {
        saveTaskId = plugin.getServerInterface().runTaskTimer(() -> saveMetaData(false), saveTicks, saveTicks);
    }

    /**
     * Stops the ongoing scheduled save task.
     */
    public void stopScheduledSave() {
        plugin.getServerInterface().cancelTask(saveTaskId);
    }

    /**
     * Adds metadata to this registry.
     *
     * @param metadata The metadata to add.
     */
    public void addMetaData(@NotNull DataType metadata) {
        blockMetadata.put(metadata.getLocation(), metadata);
        metadata.setRegistry(this);
    }

    /**
     * Removes metadata from this registry.
     *
     * @param metadata The metadata to remove.
     */
    public void removeMetaData(@NotNull DataType metadata) {
        blockMetadata.remove(metadata.getLocation());
        metadata.setRegistry(null);
    }

    /**
     * Checks whether the given block coordinates have associated metadata in this registry.
     * <br>
     * It is important to note that this only checks for metadata in this registry. The block could have other types
     * of metadata available in other registries.
     *
     * @param blockCoordinates The block coordinates to check.
     * @return True if there is metadata for the given block coordinates in this registry.
     */
    public boolean hasMetaData(@NotNull BlockCoordinates blockCoordinates) {
        return blockMetadata.containsKey(blockCoordinates);
    }

    /**
     * Returns the metadata for the given block coordinates, if any.
     *
     * @param blockCoordinates The block coordinates to get the metadata for.
     * @return The metadata for the given block coordinates or null if no metadata exists in this registry for those
     * coordinates.
     */
    @Nullable
    public DataType getMetaData(@NotNull BlockCoordinates blockCoordinates) {
        return blockMetadata.get(blockCoordinates);
    }
}
