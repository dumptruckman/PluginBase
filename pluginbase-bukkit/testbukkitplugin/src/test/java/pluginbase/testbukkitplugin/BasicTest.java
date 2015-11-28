package pluginbase.testbukkitplugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Test;
import pluginbase.testingbukkit.ServerFactory;
import pluginbase.testingbukkit.TestingServer;

import java.lang.reflect.Field;

import static org.junit.Assert.assertSame;

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
}
