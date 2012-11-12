/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyChangeEvent;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyChangeListener;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValueException;
import com.dumptruckman.minecraft.pluginbase.util.CommandUtil;
import com.dumptruckman.minecraft.pluginbase.util.MockConfig;
import com.dumptruckman.minecraft.pluginbase.util.MockMessages;
import com.dumptruckman.minecraft.pluginbase.util.MockPlugin;
import com.dumptruckman.minecraft.pluginbase.util.TestInstanceCreator;
import junit.framework.Assert;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AbstractBukkitPlugin.class, SimplePluginManager.class})
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
        File serverDirectory = new File(creator.getPlugin().getServerFolder(), "world");
        serverDirectory.mkdirs();

        // Assert debug mode is off
        Assert.assertEquals(0, (int) myPlugin.config().get(BaseConfig.DEBUG_MODE));

        Assert.assertFalse(myPlugin.config().get(BaseConfig.FIRST_RUN));

        // Send the debug command.
        CommandUtil.runCommand(plugin, mockCommandSender, "pb debug", "3");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb test");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb confirm");

        //Assert.assertTrue(MockQueuedCommand.TEST);

        CommandUtil.runCommand(plugin, mockCommandSender, "pb help");
        CommandUtil.runCommand(plugin, mockCommandSender, "pb");

        CommandUtil.runCommand(plugin, mockCommandSender, "pb version", "-p");

        Assert.assertFalse(myPlugin.config().get(BaseConfig.FIRST_RUN));

        Assert.assertEquals(3, (int) myPlugin.config().get(BaseConfig.DEBUG_MODE));
        
        myPlugin.getMessager().message(myPlugin.wrapSender(mockCommandSender), MockMessages.TEST_MESSAGE, "And a test arg");

        Assert.assertEquals(BaseConfig.LOCALE.getDefaultSerializer().serialize(BaseConfig.LOCALE.getDefault()).toString(), myPlugin.config().get(BaseConfig.LOCALE).toString());
        
        myPlugin.config().set(BaseConfig.LOCALE, Locale.CANADA);

        Assert.assertEquals(Locale.CANADA, myPlugin.config().get(BaseConfig.LOCALE));
        
        myPlugin.config().flush();
        
        Assert.assertEquals(MockConfig.SPECIFIC_TEST.getNewTypeMap(), myPlugin.config().get(MockConfig.SPECIFIC_TEST));
        Map<String, Integer> testMap = myPlugin.config().get(MockConfig.SPECIFIC_TEST);
        Assert.assertEquals(null, testMap.get("test1"));
        testMap.put("test1", 25);
        myPlugin.config().set(MockConfig.SPECIFIC_TEST, testMap);
        Assert.assertEquals(25, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST).get("test1"));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        testMap = new HashMap<String, Integer>(1);
        testMap.put("test1", 25);
        Assert.assertEquals(testMap, myPlugin.config().get(MockConfig.SPECIFIC_TEST));

        Assert.assertEquals(null, myPlugin.config().get(MockConfig.SPECIFIC_TEST, "test2"));
        myPlugin.config().set(MockConfig.SPECIFIC_TEST, "test2", 50);
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST).get("test2"));
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST, "test2"));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST).get("test2"));
        Assert.assertEquals(50, (int) myPlugin.config().get(MockConfig.SPECIFIC_TEST, "test2"));

        Assert.assertEquals(MockConfig.LIST_TEST.getNewTypeList(), myPlugin.config().get(MockConfig.LIST_TEST));
        myPlugin.config().set(MockConfig.LIST_TEST, Arrays.asList(25, 41));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        List<Integer> checkList = myPlugin.config().get(MockConfig.LIST_TEST);
        assertTrue(checkList instanceof LinkedList);
        assertTrue(checkList.contains(25) && checkList.contains(41));

        assertTrue(myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        myPlugin.config().get(MockConfig.NESTED_TEST).set(MockConfig.Nested.TEST, false);
        assertTrue(!myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        assertTrue(myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        myPlugin.config().get(MockConfig.NESTED_TEST).set(MockConfig.Nested.TEST, false);
        assertTrue(!myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));
        myPlugin.config().flush();
        CommandUtil.runCommand(plugin, mockCommandSender, "pb reload");
        assertTrue(!myPlugin.config().get(MockConfig.NESTED_TEST).get(MockConfig.Nested.TEST));

        myPlugin.config().set(MockConfig.DEBUG_MODE, 3);
        myPlugin.config().addPropertyChangeListener(MockConfig.DEBUG_MODE, new PropertyChangeListener<Integer>() {
            @Override
            public void propertyChange(PropertyChangeEvent<Integer> event) {
                assertEquals(3, (int) event.getOldValue());
                assertEquals(0, (int) event.getNewValue());
                assertTrue(!event.getDenyChange());
                event.setDenyChange(true);
                assertTrue(event.getDenyChange());
            }
        });

        try {
            myPlugin.config().set(MockConfig.DEBUG_MODE, 0);
        } catch (PropertyValueException ignore) { }
        assertEquals(3, (int) myPlugin.config().get(MockConfig.DEBUG_MODE));
    }
}
