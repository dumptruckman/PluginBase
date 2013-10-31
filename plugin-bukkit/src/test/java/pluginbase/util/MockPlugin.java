package pluginbase.util;

import pluginbase.bukkit.AbstractBukkitPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.jdbc.SpringDatabaseSettings;
import pluginbase.jdbc.SpringJdbcAgent;

import java.util.LinkedList;
import java.util.List;

public class MockPlugin extends AbstractBukkitPlugin {

    JdbcAgent jdbcAgent;

    @Override
    public void onPluginLoad() {
        MockMessages.init();
        //HelpCommand.addStaticPrefixedKey("");
    }

    @Override
    public void onPluginEnable() {
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
        try {
            jdbcAgent = SpringJdbcAgent.createAgent(loadDatabaseSettings(new SpringDatabaseSettings()), getDataFolder(), MockPlugin.class.getClassLoader());
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
    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>(super.dumpVersionInfo());
        versionInfo.add("Test");
        return versionInfo;
    }

    @NotNull
    @Override
    protected MockConfig getDefaultSettings() {
        return new MockConfig(this);
    }

    @NotNull
    @Override
    public MockConfig getSettings() {
        return (MockConfig) super.getSettings();
    }

    @NotNull
    @Override
    public JdbcAgent getJdbcAgent() {
        return jdbcAgent;
    }
}
