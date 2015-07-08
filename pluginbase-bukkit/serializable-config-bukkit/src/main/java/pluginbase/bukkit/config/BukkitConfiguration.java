package pluginbase.bukkit.config;

import com.google.common.base.Charsets;
import org.apache.commons.lang.Validate;
import pluginbase.config.ConfigSerializer;
import pluginbase.config.SerializationRegistrar;
import com.google.common.io.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.field.FieldMapper;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.messaging.SendablePluginBaseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BukkitConfiguration extends FileConfiguration {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * Creates a new {@link YamlConfiguration}, loading from the given file.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be returned.
     *
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if file is null
     */
    public static YamlConfiguration loadYamlConfig(@NotNull File file) throws SendablePluginBaseException {

        YamlConfiguration config = new YamlConfiguration();

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            config.load(file);
        } catch (FileNotFoundException ex) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.COULD_NOT_LOAD, file), ex);
        } catch (IOException ex) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.COULD_NOT_LOAD, file), ex);
        } catch (InvalidConfigurationException ex) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.COULD_NOT_LOAD, file), ex);
        }

        return config;
    }

    /**
     * Creates a new {@link YamlConfiguration}, loading from the given stream.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be returned.
     *
     * @param stream Input stream
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     */
    public static YamlConfiguration loadYamlConfig(@NotNull InputStream stream) throws SendablePluginBaseException {
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(stream);
        } catch (IOException ex) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.COULD_NOT_LOAD, stream), ex);
        } catch (InvalidConfigurationException ex) {
            throw new SendablePluginBaseException(Message.bundleMessage(Messages.COULD_NOT_LOAD, stream), ex);
        }

        return config;
    }

    @Override
    public void load(@NotNull File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        final FileInputStream stream = new FileInputStream(file);

        load(stream);
    }

    @Override
    public void load(@NotNull final InputStream stream) throws IOException, InvalidConfigurationException {
        load(new InputStreamReader(stream, Charsets.UTF_8));
    }

    @Override
    public void save(@NotNull final File file) throws IOException {
        Files.createParentDirs(file);

        String data = saveToString();

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            writer.write(data);
        }
    }

    @Override
    protected abstract String buildHeader();

    /**
     * Retrieves the object data at the specified path and attempts to insert it into an object of the given type, if
     * possible.
     * <p/>
     * If the specified path contains an object of the given type, the object at the specified path will be returned.
     * <p/>
     * If the specified path contains valid data for filling an object of the given type, this will be done and the data
     * at the specified path will be replaced by the returned object.
     *
     * @param path The path to retrieve data from.
     * @param clazz The type of object expected from the given data.
     * @param <T> The object's type.
     * @return An object from the specified path or made from data at the specified path.  If there is nothing at the
     * specified path or the data cannot be parsed into the correct type of object, null will be returned.
     */

    @Nullable
    public <T> T getAs(@NotNull String path, @NotNull Class<T> clazz) {
        Object o = get(path);
        if (o != null && o.getClass().equals(clazz)) {
            return (T) o;
        }
        if (o instanceof ConfigurationSection) {
            o = convertSectionToMap((ConfigurationSection) o);
        }
        if (o instanceof List) {
            o = convertList((List) o);
        }
        if (o != null && SerializationRegistrar.isClassRegistered(clazz)) {
            try {
                T object = (T) ConfigSerializer.deserializeAs(o, clazz);
                set(path, object);
                return object;
            } catch (Exception e) {
                e.printStackTrace(); // TODO remove
                return null;
            }
        } else if (clazz.isInstance(o)) {
            return (T) o;
        } else {
            return null;
        }
    }

    @Nullable
    public <T> T getToObject(@NotNull String path, @NotNull T destination) {
        T source = getAs(path, (Class<T>) destination.getClass());
        if (destination.equals(source)) {
            return destination;
        }
        if (source != null) {
            destination = FieldMapper.mapFields(source, destination);
            set(path, destination);
            return destination;
        } else {
            return null;
        }
    }

    private Map convertSectionToMap(@NotNull ConfigurationSection section) {
        Map<String, Object> values = section.getValues(false);
        Map<String, Object> result = new LinkedHashMap<String, Object>(values.size());
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            int lastSeparatorIndex = key.lastIndexOf(options().pathSeparator());
            if (lastSeparatorIndex >= 0) {
                key = key.substring(lastSeparatorIndex + 1, key.length());
            }
            if (value instanceof ConfigurationSection) {
                value = convertSectionToMap((ConfigurationSection) value);
            }
            result.put(key, value);
        }
        return result;
    }

    private List convertList(@NotNull List list) {
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof ConfigurationSection) {
                list.set(i, convertSectionToMap((ConfigurationSection) o));
            }
        }
        return list;
    }
}
