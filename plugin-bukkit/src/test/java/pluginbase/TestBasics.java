/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package pluginbase;

import org.bukkit.craftbukkit.v1_6_R2.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.jdbc.core.JdbcTemplate;
import pluginbase.bukkit.minecraft.BukkitTools;
import pluginbase.bukkit.command.CommandUtil;
import pluginbase.config.serializers.Serializers;
import pluginbase.messages.MessageProvider;
import pluginbase.plugin.Settings.Language;
import pluginbase.util.MockConfig;
import pluginbase.util.MockMessages;
import pluginbase.util.MockPlugin;
import pluginbase.util.TestInstanceCreator;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JavaPlugin.class, SimplePluginManager.class, CraftServer.class })
public class TestBasics {
    TestInstanceCreator creator;
    Server mockServer;
    CommandSender mockCommandSender;

    @Before
    public void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertTrue(creator.setUp());
        mockServer = creator.getServer();
        mockCommandSender = creator.getCommandSender();
    }

    @After
    public void tearDown() throws Exception {
        creator.tearDown();
    }

    @Test
    public void testEnableDebugMode() throws Exception {
        // Pull a core instance from the server.
        Plugin plugin = mockServer.getPluginManager().getPlugin("PluginBase");
        MockPlugin myPlugin = (MockPlugin) plugin;

        // Make sure Core is not null
        assertNotNull(plugin);

        // Make sure Core is enabled
        assertTrue(plugin.isEnabled());

        // Make a fake server folder to fool MV into thinking a world folder exists.
        File serverDirectory = new File(creator.getPlugin().getServerInterface().getWorldContainer(), "world");
        serverDirectory.mkdirs();

        // Assert debug mode is off
        assertEquals(0, (myPlugin.getSettings().getDebugLevel()));

        assertFalse(myPlugin.getSettings().isFirstRun());

        // Send the debug command.
        CommandUtil.runCommand(plugin, mockCommandSender, "pb debug", "3");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb test");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb confirm");

        //Assert.assertTrue(MockQueuedCommand.TEST);

        CommandUtil.runCommand(plugin, mockCommandSender, "pb help");
        CommandUtil.runCommand(plugin, mockCommandSender, "pb");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb version", "-p");

        assertFalse(myPlugin.getSettings().isFirstRun());

        assertEquals(3, myPlugin.getSettings().getDebugLevel());
        
        myPlugin.getMessager().message(BukkitTools.wrapSender(mockCommandSender), MockMessages.TEST_MESSAGE, "And a test arg");

        assertEquals(Serializers.getSerializer(Language.LocaleSerializer.class).serialize(MessageProvider.DEFAULT_LOCALE), myPlugin.getSettings().getLanguageSettings().getLocale().toString());
        
        myPlugin.getSettings().getLanguageSettings().setLocale(Locale.CANADA);

        assertEquals(Locale.CANADA, myPlugin.getSettings().getLanguageSettings().getLocale());
        
        myPlugin.saveConfig();

        MockConfig defaults = new MockConfig(myPlugin.getPluginBase());

        assertEquals(defaults.specificTest, myPlugin.getSettings().getProperty("specific_test"));
        Map<String, Integer> testMap = myPlugin.getSettings().specificTest;
        assertEquals(null, testMap.get("test1"));
        testMap.put("test1", 25);
        myPlugin.saveConfig();
        myPlugin.reloadConfig();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        testMap = new HashMap<String, Integer>();
        testMap.put("test1", 25);
        for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
            assertEquals(entry.getValue(), myPlugin.getSettings().specificTest.get(entry.getKey()));
        }
        for (Map.Entry<String, Integer> entry : myPlugin.getSettings().specificTest.entrySet()) {
            assertEquals(entry.getValue(), testMap.get(entry.getKey()));
        }

        assertEquals(defaults.listTest, myPlugin.getSettings().getProperty("list_test"));
        myPlugin.getSettings().listTest = Arrays.asList(25, 41);
        myPlugin.saveConfig();
        myPlugin.reloadConfig();
        List<Integer> checkList = myPlugin.getSettings().listTest;
        assertTrue(checkList.contains(25) && checkList.contains(41));

        assertTrue(myPlugin.getSettings().nested.test);
        myPlugin.getSettings().nested.test = false;
        assertTrue(!myPlugin.getSettings().nested.test);
        myPlugin.reloadConfig();
        assertTrue((Boolean) myPlugin.getSettings().getProperty("nested.nested_test"));
        myPlugin.getSettings().nested.test = false;
        assertTrue(!myPlugin.getSettings().nested.test);
        myPlugin.saveConfig();
        myPlugin.reloadConfig();
        assertTrue(!myPlugin.getSettings().nested.test);


        assertNotNull(myPlugin.getJdbcAgent());
        JdbcTemplate testCreate = new JdbcTemplate(myPlugin.getJdbcAgent().getDataSource());
        testCreate.update("CREATE TABLE COMPANY " +
                   "(ID INT PRIMARY KEY     NOT NULL," +
                   " NAME           TEXT    NOT NULL, " +
                   " AGE            INT     NOT NULL, " +
                   " ADDRESS        CHAR(50), " +
                   " SALARY         REAL)");
    }
}
