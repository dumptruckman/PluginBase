package pluginbase.bukkit.config;

import pluginbase.config.ConfigSerializer;
import pluginbase.config.SerializationRegistrar;
import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

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
    public static YamlConfiguration loadYamlConfig(File file) {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file , ex);
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
    public static YamlConfiguration loadYamlConfig(InputStream stream) {
        Validate.notNull(stream, "Stream cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(stream);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }

        return config;
    }

    @Override
    public void load(@NotNull final InputStream stream) throws IOException, InvalidConfigurationException {
        InputStreamReader reader = new InputStreamReader(stream, UTF8);
        StringBuilder builder = new StringBuilder();
        BufferedReader input = new BufferedReader(reader);

        try {
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }

        loadFromString(builder.toString());
    }

    @Override
    public void save(@NotNull final File file) throws IOException {
        Files.createParentDirs(file);

        final String data = saveToString();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), UTF8));
            writer.write(data);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    protected abstract String buildHeader();

    /**
     * Retrieves the object data at the specified path and attempts to insert it into the given object, if possible.
     * <p/>
     * If the specified path contains an object of the same type as the given object, the object at the specified path
     * will be returned.
     * <p/>
     * If the specified path contains valid data for filling the given object, this will be done and the data the
     * specified path will be replaced by the given object.
     *
     * @param path The path to retrieve data from.
     * @param object The object to insert the data into.
     * @param <T> The object's type.
     * @return An object from the specified path or made from data at the specified path.  If there is nothing at the
     * specified path or the data cannot be parsed into the correct type of object, null will be returned.
     */
    @Nullable
    public <T> T getToObject(@NotNull String path, @NotNull T object) {
        Object o = get(path);
        if (o != null && o.getClass().equals(object.getClass())) {
            return (T) o;
        }
        if (o instanceof ConfigurationSection) {
            o = convertSectionToMap((ConfigurationSection) o);
        }
        if (SerializationRegistrar.isClassRegistered(object.getClass()) && o instanceof Map) {
            try {
                object = (T) ConfigSerializer.deserializeToObject((Map) o, object);
                set(path, object);
                return object;
            } catch (Exception e) {
                return null;
            }
        } else if (object.getClass().isInstance(o)) {
            return (T) o;
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
}
