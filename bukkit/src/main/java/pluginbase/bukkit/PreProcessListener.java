package pluginbase.bukkit;

import pluginbase.command.CommandProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

// Unused currently... Need to determine if needed.
class PreProcessListener implements Listener {

    private final CommandProvider plugin;

    PreProcessListener(@NotNull final CommandProvider plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void playerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final String[] split = plugin.getCommandHandler().commandDetection(event.getMessage().substring(1).split(" "));
        final String newMessage = "/" + StringUtil.joinString(split, " ");
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
        final String[] split = plugin.getCommandHandler().commandDetection(event.getCommand().split(" "));
        final String newMessage = StringUtil.joinString(split, " ");
        if (!newMessage.equals(event.getCommand())) {
            event.setCommand(newMessage);
            Bukkit.dispatchCommand(event.getSender(), event.getCommand());
        }
    }
}
