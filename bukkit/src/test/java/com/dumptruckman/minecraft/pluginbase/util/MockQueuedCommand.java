package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.plugin.command.QueuedPluginCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.List;

public class MockQueuedCommand extends QueuedPluginCommand<MockPlugin> {

    public static boolean TEST = false;

    public MockQueuedCommand(MockPlugin plugin) {
        super(plugin);
        addPrefixedKey(" test");
        setPermission(new Permission("test"));
    }

    @Override
    public void preConfirm(CommandSender sender, List<String> args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onConfirm(CommandSender sender, List<String> args) {
        TEST = true;
    }

    @Override
    public void onExpire(CommandSender sender, List<String> args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
