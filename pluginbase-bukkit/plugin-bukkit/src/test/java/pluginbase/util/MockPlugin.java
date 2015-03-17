package pluginbase.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pluginbase.bukkit.BukkitPluginAgent;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.jdbc.SpringDatabaseSettings;
import pluginbase.jdbc.SpringJdbcAgent;
import pluginbase.messages.messaging.Messager;
import pluginbase.plugin.PluginBase;
import pluginbase.plugin.PluginInfo;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;
import pluginbase.plugin.VersionInfoModifier;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

public class MockPlugin extends JavaPlugin {

    JdbcAgent jdbcAgent;

    BukkitPluginAgent pluginAgent = BukkitPluginAgent.getPluginAgent(MockPlugin.class, this, "pb");

    public void onLoad() {
        pluginAgent.setVersionInfoModifier(new VersionInfoExtras());
        pluginAgent.setDefaultSettingsCallable(new Callable<Settings>() {
            @Override
            public Settings call() throws Exception {
                return new MockConfig(pluginAgent.getPluginBase());
            }
        });
        pluginAgent.setJdbcAgentCallable(new Callable<JdbcAgent>() {
            @Override
            public JdbcAgent call() throws Exception {
                return jdbcAgent;
            }
        });
        pluginAgent.registerMessages(MockMessages.class);
        pluginAgent.loadPluginBase();
    }

    @Override
    public void onEnable() {
        pluginAgent.enablePluginBase();
        try {
            File configFile = new File(getDataFolder(), "db_config.yml");
            jdbcAgent = SpringJdbcAgent.createAgent(pluginAgent.loadDatabaseSettings(new SpringDatabaseSettings()), getDataFolder(), MockPlugin.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        pluginAgent.disablePluginBase();
    }

    private static class VersionInfoExtras implements VersionInfoModifier {
        @NotNull
        @Override
        public List<String> modifyVersionInfo(@NotNull List<String> versionInfo) {
            versionInfo.add("Test");
            return versionInfo;
        }
    }

    public PluginInfo getPluginInfo() {
        return getPluginBase().getPluginInfo();
    }

    public ServerInterface getServerInterface() {
        return getPluginBase().getServerInterface();
    }

    public MockConfig getSettings() {
        return (MockConfig) getPluginBase().getSettings();
    }

    public Messager getMessager() {
        return getPluginBase().getMessager();
    }

    public PluginBase getPluginBase() {
        return pluginAgent.getPluginBase();
    }

    public JdbcAgent getJdbcAgent() {
        return getPluginBase().getJdbcAgent();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return pluginAgent.callCommand(sender, command, label, args);
    }
}
