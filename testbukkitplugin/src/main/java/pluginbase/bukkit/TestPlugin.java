package pluginbase.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import pluginbase.bukkit.commands.TealPeaPieCommand;
import pluginbase.bukkit.commands.TealPieCommand;
import pluginbase.bukkit.commands.Test2Command;
import pluginbase.bukkit.commands.TestCommand;
import pluginbase.bukkit.pie.Pie;
import pluginbase.bukkit.pie.PieProperties;
import pluginbase.config.SerializationRegistrar;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.jdbc.SpringDatabaseSettings;
import pluginbase.jdbc.SpringJdbcAgent;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.plugin.Settings;

import java.util.List;
import java.util.concurrent.Callable;

public class TestPlugin extends JavaPlugin {

    static {
        SerializationRegistrar.registerClass(TestConfig.class);
        SerializationRegistrar.registerClass(Pie.class);
        SerializationRegistrar.registerClass(PieProperties.class);
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

        List<Pie> pieList = getSettings().getPieList();
        if (pieList.isEmpty()) {
            System.out.println("Baking some pies...");
            Pie pie = new Pie(new PieProperties());
            pie.setName("Pie 1");
            pie.setNumber(1);
            pie.setTime(System.nanoTime());
            pieList.add(pie);
            pie = new Pie(new PieProperties());
            pie.setName("Pie 2");
            pie.setNumber(2);
            pie.setTime(System.nanoTime());
            pieList.add(pie);
            try {
                pluginAgent.getPluginBase().saveSettings();
            } catch (SendablePluginBaseException e) {
                e.printStackTrace();
            }
        }
    }

    public TestConfig getSettings() {
        return (TestConfig) pluginAgent.getPluginBase().getSettings();
    }

    public void onDisable() {
        pluginAgent.disablePluginBase();
    }

    @Nullable
    public JdbcAgent getJdbcAgent() {
        return pluginAgent.getPluginBase().getJdbcAgent();
    }
}
