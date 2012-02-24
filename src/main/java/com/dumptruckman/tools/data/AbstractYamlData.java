package com.dumptruckman.tools.data;

import com.dumptruckman.tools.util.Logging;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public abstract class AbstractYamlData {

    protected static final String YML = ".yml";
    protected File dataFile = null;

    public AbstractYamlData(JavaPlugin plugin) throws IOException {
        // Make the data folders
        if (plugin.getDataFolder().mkdirs()) {
            Logging.info("Created data folder.");
        }

        // Check if the config file exists.  If not, create it.
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            if (!dataFile.createNewFile()) {
                throw new IOException("Could not create data file!");
            }
        }
    }
}
