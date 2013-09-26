package pluginbase.bukkit.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;

public class MemoryConfigurationTest extends ConfigurationTest {
    @Override
    public Configuration getConfig() {
        return new MemoryConfiguration();
    }
}
