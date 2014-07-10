package pluginbase.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializationRegistrar;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.jdbc.SpringDatabaseSettings;
import pluginbase.jdbc.SpringJdbcAgent;
import pluginbase.plugin.Settings;

import java.util.concurrent.Callable;

public class TestPlugin extends JavaPlugin {

    static {
        SerializationRegistrar.registerClass(TestConfig.class);
    }

    JdbcAgent jdbcAgent;

    final BukkitPluginAgent pluginAgent;

    public TestPlugin() {
        pluginAgent = BukkitPluginAgent.getPluginAgent(TestPlugin.class, this, "pb");
        pluginAgent.setDefaultSettingsCallable(new Callable<Settings>() {
            @Override
            public Settings call() throws Exception {
                return new TestConfig();
            }
        });

        pluginAgent.registerMessages(Language.class);

        pluginAgent.registerCommand(TestCommand.class);
        pluginAgent.registerCommand(Test2Command.class);
        pluginAgent.registerCommand(TealPieCommand.class);
        pluginAgent.registerCommand(TealPeaPieCommand.class);
    }

    public void onLoad() {
        pluginAgent.setJdbcAgentCallable(new Callable<JdbcAgent>() {
            @Override
            public JdbcAgent call() throws Exception {
                return jdbcAgent;
            }
        });
        pluginAgent.loadPluginBase();
    }

    @Override
    public void onEnable() {
        pluginAgent.enablePluginBase();
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
        try {
            jdbcAgent = SpringJdbcAgent.createAgent(pluginAgent.loadDatabaseSettings(new SpringDatabaseSettings()), getDataFolder(), getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        pluginAgent.disablePluginBase();
    }

    @Nullable
    public JdbcAgent getJdbcAgent() {
        return pluginAgent.getPluginBase().getJdbcAgent();
    }
}
