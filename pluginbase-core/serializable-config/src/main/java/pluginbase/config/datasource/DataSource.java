package pluginbase.config.datasource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.messaging.SendablePluginBaseException;

/**
 * A storage medium for a single data object.
 */
public interface DataSource {

    /**
     * Attempts to extract the given type of data out of this storage medium.
     *
     * @return The data from the storage medium or null if no data exists.
     * @param <ObjectType> The type that the data will be deserialized/cast as when extracted.
     * @throws SendablePluginBaseException if any sort of error occurs with loading, reading, parsing or casting the data.
     */
    @Nullable
    <ObjectType> ObjectType load(Class<ObjectType> wantedType) throws SendablePluginBaseException;

    /**
     * Attempts to extract the data out of this storage medium.
     *
     * @return The data from the storage medium or null if no data exists.
     * @throws SendablePluginBaseException if any sort of error occurs with loading, reading, or parsing the data.
     */
    @Nullable
    Object load() throws SendablePluginBaseException;

    /**
     * Attempts to extract the data out of this storage medium and insert it into the given destination object.
     * <br/>
     * Note that if the data is incomplete in the storage medium, it will not replace those missing values in the given
     * destination object.
     *
     * @param destination The object to populated with the data from this storage medium.
     * @param <ObjectType> The return
     * @return The destination object populated with the data from this storage medium or null if no data exists.
     * @throws SendablePluginBaseException if any sort of error occurs with loading, reading, parsing or casting the data.
     */
    @Nullable
    <ObjectType> ObjectType loadToObject(@NotNull ObjectType destination) throws SendablePluginBaseException;

    /**
     * Saves the given object to this storage medium.
     *
     * @param object The object to place into this storage medium.
     * @throws SendablePluginBaseException if any sort of error occurs with writing or generating the configuration.
     */
    void save(Object object) throws SendablePluginBaseException;
}
