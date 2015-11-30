package pluginbase.testbukkitplugin;

import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import pluginbase.bukkit.BukkitPluginAgent;
import pluginbase.testplugin.Language;
import pluginbase.testplugin.TestConfig;
import pluginbase.testplugin.commands.TealPeaPieCommand;
import pluginbase.testbukkitplugin.command.TealPieCommand;
import pluginbase.testplugin.commands.Test2Command;
import pluginbase.testplugin.commands.TestCommand;
import pluginbase.testplugin.pie.Pie;
import pluginbase.testplugin.pie.PieProperties;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.jdbc.SpringDatabaseSettings;
import pluginbase.jdbc.SpringJdbcAgent;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.plugin.Settings;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

public class TestPlugin extends JavaPlugin {

    JdbcAgent jdbcAgent;

    BukkitPluginAgent pluginAgent;

    TestPlugin(PluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, server, description, dataFolder, file);
        init();
    }

    public TestPlugin() {
        init();
    }

    private void init() {
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

    public void reloadConfig() {
        pluginAgent.getPluginBase().reloadConfig();
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
