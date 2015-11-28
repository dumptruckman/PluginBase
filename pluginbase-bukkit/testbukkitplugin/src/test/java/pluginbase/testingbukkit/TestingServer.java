package pluginbase.testingbukkit;

import org.bukkit.Server;
import pluginbase.testingbukkit.plugin.TestingPluginManager;

public interface TestingServer extends Server {

    void loadDefaultWorlds();

    TestingPluginManager getPluginManager();

    void setAllowEnd(boolean allowEnd);

    void setAllowNether(boolean allowNether);

    void setTicksPerMonsterSpawn(int ticksPerMonsterSpawn);

    void setTicksPerAnimalSpawn(int ticksPerAnimalSpawn);
}
