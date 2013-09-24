package com.dumptruckman.minecraft.pluginbase.config.bukkit;

import com.dumptruckman.minecraft.pluginbase.config.ConfigSerializer;
import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public <T> T getToObject(@NotNull String path, @NotNull T object) {
        Object o = getValues(true);
        if (o instanceof ConfigurationSection) {
            System.out.println("convert");
            o = ((ConfigurationSection) o).getValues(true);
        }
        if (object instanceof Map && o instanceof Map && !object.equals(o)) {
            ((Map) object).clear();
            ((Map) object).putAll((Map) o);
            return object;
        }
        if (o instanceof Map) {
            return ConfigSerializer.deserializeToObject((Map) o, object);
        } else if (o != null && o.getClass().equals(object.getClass())) {
            return ConfigSerializer.deserializeToObject(ConfigSerializer.serialize(o), object);
        } else {
            return object;
        }
    }
}
