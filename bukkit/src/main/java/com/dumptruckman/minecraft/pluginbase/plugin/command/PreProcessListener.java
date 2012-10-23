package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.sk89q.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class PreProcessListener implements Listener {

    private final PluginBase plugin;

    public PreProcessListener(final PluginBase plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void playerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        System.out.println("preprocessing: " + event.getMessage());
        final String[] split = plugin.getCommandHandler().commandDetection(event.getMessage().substring(1).split(" "));
        final String newMessage = "/" + StringUtil.joinString(split, " ");
        System.out.println("produced: " + newMessage);
        if (!newMessage.equals(event.getMessage())) {
            event.setMessage(newMessage);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                if (event.getMessage().length() > 0) {
                    Bukkit.dispatchCommand(event.getPlayer(), event.getMessage().substring(1));
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerCommandPreprocess(ServerCommandEvent event) {
        System.out.println("processing: " + event.getCommand());
        final String[] split = plugin.getCommandHandler().commandDetection(event.getCommand().split(" "));
        final String newMessage = StringUtil.joinString(split, " ");
        System.out.println("produced: " + newMessage);
        if (!newMessage.equals(event.getCommand())) {
            event.setCommand(newMessage);
            Bukkit.dispatchCommand(event.getSender(), event.getCommand());
        }
    }
}
