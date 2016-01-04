package pluginbase.testspongeplugin;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.jdbc.SpringDatabaseSettings;
import pluginbase.jdbc.SpringJdbcAgent;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.plugin.Settings;
import pluginbase.sponge.SpongePluginAgent;
import pluginbase.testplugin.Language;
import pluginbase.testplugin.TestConfig;
import pluginbase.testplugin.commands.TealPeaPieCommand;
import pluginbase.testplugin.commands.Test2Command;
import pluginbase.testplugin.commands.TestCommand;
import pluginbase.testplugin.pie.Pie;
import pluginbase.testplugin.pie.PieProperties;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@Plugin(id = "TestSpongePlugin", name = "TestSpongePlugin")
public class TestPlugin {

    JdbcAgent jdbcAgent;

    SpongePluginAgent pluginAgent;

    @Inject
    private PluginContainer pluginContainer;
    @Inject
    @ConfigDir(sharedRoot = false)
    private File dataFolder;
    @Inject
    private Game game;

    @Subscribe
    private void preInitialization(GamePreInitializationEvent event) {
        pluginAgent = SpongePluginAgent.getPluginAgent(game, TestPlugin.class, this, pluginContainer, "pb", dataFolder);
        pluginAgent.setDefaultSettingsCallable(new Callable<Settings>() {
            @Override
            public Settings call() throws Exception {
                return new TestConfig();
            }
        });

        pluginAgent.registerMessages(Language.class);
    }

    @Subscribe
    private void initialization(GameInitializationEvent event) {
        pluginAgent.registerCommand(TestCommand.class);
        pluginAgent.registerCommand(Test2Command.class);
        pluginAgent.registerCommand(TealPeaPieCommand.class);

        pluginAgent.setJdbcAgentCallable(new Callable<JdbcAgent>() {
            @Override
            public JdbcAgent call() throws Exception {
                return jdbcAgent;
            }
        });
        pluginAgent.loadPluginBase();

        pluginAgent.enablePluginBase();
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
        try {
            jdbcAgent = SpringJdbcAgent.createAgent(pluginAgent.loadDatabaseSettings(new SpringDatabaseSettings()), dataFolder, getClass().getClassLoader());
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

    @Subscribe
    private void serverStopping(GameStoppingEvent event) {
        pluginAgent.disablePluginBase();
    }

    @Nullable
    public JdbcAgent getJdbcAgent() {
        return pluginAgent.getPluginBase().getJdbcAgent();
    }
}
