/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.minecraft.server.BukkitServerInterface;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPluginInfo;
import junit.framework.Assert;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class TestInstanceCreator {
    private AbstractBukkitPlugin plugin;
    private Server mockServer;
    private CommandSender commandSender;

    public Map<String, Player> players = new HashMap<String, Player>();

    public static final File pluginDirectory = new File("bin/test/server/plugins/plugintest");
    public static final File serverDirectory = new File("bin/test/server");
    public static final File worldsDirectory = new File("bin/test/server");

    public boolean setUp() {
        FileUtils.deleteFolder(serverDirectory);
        try {
            pluginDirectory.mkdirs();
            Assert.assertTrue(pluginDirectory.exists());

            MockGateway.MOCK_STANDARD_METHODS = false;

            plugin = PowerMockito.spy(new MockPlugin());
            PowerMockito.doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    return null; // don't run metrics in tests
                }
            }).when(plugin, "setupMetrics");
            PowerMockito.doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    return null; // don't run metrics in tests
                }
            }).when(plugin, "startMetrics");

            // Let's let all MV files go to bin/test
            doReturn(pluginDirectory).when(plugin).getDataFolder();

            // Return a fake PDF file.
            PluginDescriptionFile pdf = new PluginDescriptionFile("PluginBase", "1.1",
                    "com.dumptruckman.minecraft.pluginbase.util.MockPlugin");
            doReturn(pdf).when(plugin).getDescription();
            doReturn(true).when(plugin).isEnabled();
            plugin.setServerFolder(serverDirectory);

            when(plugin.getPluginInfo()).thenReturn(new BukkitPluginInfo(plugin));

            // Add Core to the list of loaded plugins
            JavaPlugin[] plugins = new JavaPlugin[] { plugin };

            // Make some fake folders to fool the fake MV into thinking these worlds exist
            File worldNormalFile = new File(plugin.getServerFolder(), "world");
            Util.log("Creating world-folder: " + worldNormalFile.getAbsolutePath());
            worldNormalFile.mkdirs();
            File worldNetherFile = new File(plugin.getServerFolder(), "world_nether");
            Util.log("Creating world-folder: " + worldNetherFile.getAbsolutePath());
            worldNetherFile.mkdirs();
            File worldSkylandsFile = new File(plugin.getServerFolder(), "world_the_end");
            Util.log("Creating world-folder: " + worldSkylandsFile.getAbsolutePath());
            worldSkylandsFile.mkdirs();

            // Initialize the Mock server.
            mockServer = mock(Server.class);
            when(mockServer.getName()).thenReturn("TestBukkit");
            Logger.getLogger("Minecraft").setParent(Util.logger);
            when(mockServer.getLogger()).thenReturn(Util.logger);
            when(mockServer.getVersion()).thenReturn("vTest123");
            when(mockServer.getWorldContainer()).thenReturn(worldsDirectory);
            Answer<Player> playerAnswer = new Answer<Player>() {
                public Player answer(InvocationOnMock invocation) throws Throwable {
                    String arg;
                    try {
                        arg = (String) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    Player player = players.get(arg);
                    if (player == null) {
                        player = mock(Player.class);
                        when(player.getName()).thenReturn(arg);
                        players.put(arg, player);
                    }
                    return player;
                }
            };
            when(mockServer.getPlayer(anyString())).thenAnswer(playerAnswer);
            when(mockServer.getOfflinePlayer(anyString())).thenAnswer(playerAnswer);
            when(mockServer.getOfflinePlayers()).thenAnswer(new Answer<OfflinePlayer[]>() {
                public OfflinePlayer[] answer(InvocationOnMock invocation) throws Throwable {
                    return players.values().toArray(new Player[players.values().size()]);
                }
            });
            when(mockServer.getOnlinePlayers()).thenAnswer(new Answer<Player[]>() {
                public Player[] answer(InvocationOnMock invocation) throws Throwable {
                    return players.values().toArray(new Player[players.values().size()]);
                }
            });

            // Mock the Plugin Manager
            PluginManager mockPluginManager = Mockito.spy(new SimplePluginManager(mockServer, new SimpleCommandMap(mockServer)));
            when(mockPluginManager.getPlugins()).thenReturn(plugins);
            when(mockPluginManager.getPlugin("PluginBase")).thenReturn(plugin);
            when(mockPluginManager.getPermission(anyString())).thenReturn(null);
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            }).when(mockPluginManager).registerEvents(any(Listener.class), any(Plugin.class));

            // Give the server some worlds
            when(mockServer.getWorld(anyString())).thenAnswer(new Answer<World>() {
                public World answer(InvocationOnMock invocation) throws Throwable {
                    String arg;
                    try {
                        arg = (String) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    return MockWorldFactory.getWorld(arg);
                }
            });

            when(mockServer.getWorlds()).thenAnswer(new Answer<List<World>>() {
                public List<World> answer(InvocationOnMock invocation) throws Throwable {
                    return MockWorldFactory.getWorlds();
                }
            });

            when(mockServer.getPluginManager()).thenReturn(mockPluginManager);

            when(mockServer.createWorld(Matchers.isA(WorldCreator.class))).thenAnswer(
                    new Answer<World>() {
                        public World answer(InvocationOnMock invocation) throws Throwable {
                            WorldCreator arg;
                            try {
                                arg = (WorldCreator) invocation.getArguments()[0];
                            } catch (Exception e) {
                                return null;
                            }
                            // Add special case for creating null worlds.
                            // Not sure I like doing it this way, but this is a special case
                            if (arg.name().equalsIgnoreCase("nullworld")) {
                                return MockWorldFactory.makeNewNullMockWorld(arg.name(), arg.environment(), arg.type());
                            }
                            return MockWorldFactory.makeNewMockWorld(arg.name(), arg.environment(), arg.type());
                        }
                    });

            when(mockServer.unloadWorld(anyString(), anyBoolean())).thenReturn(true);

            // add mock scheduler
            BukkitScheduler mockScheduler = mock(BukkitScheduler.class);
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong())).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return null;
                    }
                    arg.run();
                    return null;
                }});
            when(mockScheduler.runTaskLater(any(Plugin.class), any(Runnable.class), anyLong())).
                    thenAnswer(new Answer<BukkitTask>() {
                        public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                            Runnable arg;
                            try {
                                arg = (Runnable) invocation.getArguments()[1];
                            } catch (Exception e) {
                                return null;
                            }
                            arg.run();
                            BukkitTask mockTask = mock(BukkitTask.class);
                            when(mockTask.getTaskId()).thenReturn(1);
                            return mockTask;
                        }});
            when(mockScheduler.runTaskLaterAsynchronously(any(Plugin.class), any(Runnable.class), anyLong())).
                    thenAnswer(new Answer<BukkitTask>() {
                        public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                            Runnable arg;
                            try {
                                arg = (Runnable) invocation.getArguments()[1];
                            } catch (Exception e) {
                                return null;
                            }
                            arg.run();
                            BukkitTask mockTask = mock(BukkitTask.class);
                            when(mockTask.getTaskId()).thenReturn(1);
                            return mockTask;
                        }});
            when(mockScheduler.runTask(any(Plugin.class), any(Runnable.class))).
                    thenAnswer(new Answer<BukkitTask>() {
                        public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                            Runnable arg;
                            try {
                                arg = (Runnable) invocation.getArguments()[1];
                            } catch (Exception e) {
                                return null;
                            }
                            arg.run();
                            BukkitTask mockTask = mock(BukkitTask.class);
                            when(mockTask.getTaskId()).thenReturn(1);
                            return mockTask;
                        }});
            when(mockScheduler.runTaskAsynchronously(any(Plugin.class), any(Runnable.class))).
                    thenAnswer(new Answer<BukkitTask>() {
                        public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                            Runnable arg;
                            try {
                                arg = (Runnable) invocation.getArguments()[1];
                            } catch (Exception e) {
                                return null;
                            }
                            arg.run();
                            BukkitTask mockTask = mock(BukkitTask.class);
                            when(mockTask.getTaskId()).thenReturn(1);
                            return mockTask;
                        }});
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class))).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return null;
                    }
                    arg.run();
                    return null;
                }});
            when(mockServer.getScheduler()).thenReturn(mockScheduler);

            // Set server
            Field serverfield = JavaPlugin.class.getDeclaredField("server");
            serverfield.setAccessible(true);
            serverfield.set(plugin, mockServer);

            when(plugin.getServerInterface()).thenReturn(new BukkitServerInterface(mockServer));

            /*
            // Set worldManager
            WorldManager wm = PowerMockito.spy(new WorldManager(core));
            Field worldmanagerfield = MultiverseCore.class.getDeclaredField("worldManager");
            worldmanagerfield.setAccessible(true);
            worldmanagerfield.set(core, wm);

            // Set playerListener
            MVPlayerListener pl = PowerMockito.spy(new MVPlayerListener(core));
            Field playerlistenerfield = MultiverseCore.class.getDeclaredField("playerListener");
            playerlistenerfield.setAccessible(true);
            playerlistenerfield.set(core, pl);

            // Set entityListener
            MVEntityListener el = PowerMockito.spy(new MVEntityListener(core));
            Field entitylistenerfield = MultiverseCore.class.getDeclaredField("entityListener");
            entitylistenerfield.setAccessible(true);
            entitylistenerfield.set(core, el);

            // Set weatherListener
            MVWeatherListener wl = PowerMockito.spy(new MVWeatherListener(core));
            Field weatherlistenerfield = MultiverseCore.class.getDeclaredField("weatherListener");
            weatherlistenerfield.setAccessible(true);
            weatherlistenerfield.set(core, wl);
            */

            // Init our command sender
            final Logger commandSenderLogger = Logger.getLogger("CommandSender");
            commandSenderLogger.setParent(Util.logger);
            commandSender = mock(Player.class);
            doAnswer(new Answer<Void>() {
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    commandSenderLogger.info(ChatColor.stripColor((String) invocation.getArguments()[0]));
                    return null;
                }}).when(commandSender).sendMessage(anyString());
            when(commandSender.getServer()).thenReturn(mockServer);
            when(commandSender.getName()).thenReturn("MockCommandSender");
            when(commandSender.isPermissionSet(anyString())).thenReturn(true);
            when(commandSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(commandSender.hasPermission(anyString())).thenReturn(true);
            when(commandSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(commandSender.addAttachment(plugin)).thenReturn(null);
            when(commandSender.isOp()).thenReturn(true);

            Bukkit.setServer(mockServer);

            // Load Multiverse Core
            plugin.onLoad();

            // Enable it.
            plugin.onEnable();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean tearDown() {
        /*
        List<MultiverseWorld> worlds = new ArrayList<MultiverseWorld>(core.getMVWorldManager()
                .getMVWorlds());
        for (MultiverseWorld world : worlds) {
            core.getMVWorldManager().deleteWorld(world.getName());
        }
        */

        try {
            Field serverField = Bukkit.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(Class.forName("org.bukkit.Bukkit"), null);
        } catch (Exception e) {
            Util.log(Level.SEVERE,
                    "Error while trying to unregister the server from Bukkit. Has Bukkit changed?");
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return false;
        }

        //FileUtils.deleteFolder(serverDirectory);
        MockWorldFactory.clearWorlds();

        return true;
    }

    public AbstractBukkitPlugin getPlugin() {
        return this.plugin;
    }

    public Server getServer() {
        return this.mockServer;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }
}
