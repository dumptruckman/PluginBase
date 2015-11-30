package pluginbase.testbukkitplugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Test;
import pluginbase.config.datasource.DataSource;
import pluginbase.config.datasource.hocon.HoconDataSource;
import pluginbase.messages.MessageProvider;
import pluginbase.plugin.Settings;
import pluginbase.testingbukkit.ServerFactory;
import pluginbase.testingbukkit.TestingServer;
import pluginbase.testplugin.TestConfig;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;

import static org.junit.Assert.*;

public class BasicTest extends PluginTest {

    private Server server = null;
    protected TestPlugin plugin;

    protected void extraSetup() throws Exception {
        reloadServer();
    }

    protected void extraCleanup() throws Exception {

    }

    public void reloadServer() throws Exception {
        if (server != null) {
            server.shutdown();
        }
        server = prepareBukkit(new PluginDescriptionFile("TestPlugin", "Test", TestPlugin.class.getName()));
        plugin = (TestPlugin) server.getPluginManager().getPlugin("TestPlugin");
    }

    private static Server prepareBukkit(PluginDescriptionFile... plugins) throws Exception {
        final TestingServer testingServer = ServerFactory.createTestingServer();
        System.out.println("Testing server created");

        Field field = Bukkit.class.getDeclaredField("server");
        field.setAccessible(true);
        field.set(null, testingServer);
        assertSame(testingServer, Bukkit.getServer());

        for (PluginDescriptionFile pluginInfo : plugins) {
            testingServer.getPluginManager().loadPlugin(pluginInfo);
        }
        testingServer.loadDefaultWorlds();
        for (Plugin plugin : testingServer.getPluginManager().getPlugins()) {
            testingServer.getPluginManager().enablePlugin(plugin);
        }

        return testingServer;
    }

    @Test
    public void testNoErrors() { }

    @Test
    public void testLocaleChange() throws Exception {
        assertEquals(MessageProvider.DEFAULT_LOCALE, plugin.getSettings().getLocale());
        DataSource dataSource = HoconDataSource.builder().setFile(new File(plugin.getDataFolder(), "plugin.conf")).build();
        Settings settings = dataSource.load(TestConfig.class);
        assertNotNull(settings);
        settings.setLocale(Locale.CANADA_FRENCH);
        assertEquals(Locale.CANADA_FRENCH, settings.getLocale());
        dataSource.save(settings);
        plugin.reloadConfig();
        assertEquals(Locale.CANADA_FRENCH, plugin.getSettings().getLocale());
    }
}
