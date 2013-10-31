package pluginbase.bukkit;

import org.jetbrains.annotations.NotNull;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.jdbc.SpringDatabaseSettings;
import pluginbase.jdbc.SpringJdbcAgent;

public class TestPlugin extends AbstractBukkitPlugin {

    JdbcAgent jdbcAgent;

    @Override
    public void onPluginEnable() {
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
        try {
            jdbcAgent = SpringJdbcAgent.createAgent(loadDatabaseSettings(new SpringDatabaseSettings()), getDataFolder(), getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @NotNull
    @Override
    public JdbcAgent getJdbcAgent() {
        return jdbcAgent;
    }
}
