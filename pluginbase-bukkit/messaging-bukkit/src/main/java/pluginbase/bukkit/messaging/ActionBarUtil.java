package pluginbase.bukkit.messaging;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility for sending messages to players via the action bar.
 */
public class ActionBarUtil {

    private static final Map<Player, BukkitTask> PENDING_MESSAGES = new HashMap<>();

    /**
     * Sends a message to the player's action bar.
     * <br>
     * The message will appear above the player's hot bar for 2 seconds and then fade away over 1 second.
     *
     * @param bukkitPlayer the player to send the message to.
     * @param message the message to send.
     */
    public static void sendActionBarMessage(@NotNull Player bukkitPlayer, @NotNull String message) {
        bukkitPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

    /**
     * Sends a message to the player's action bar that lasts for an extended duration.
     * <br>
     * The message will appear above the player's hot bar for the specified duration and fade away during the last
     * second of the duration.
     * <br>
     * Only one long duration message can be sent at a time per player. If a new message is sent via this message
     * any previous messages still being displayed will be replaced.
     *
     * @param bukkitPlayer the player to send the message to.
     * @param message the message to send.
     * @param duration the duration the message should be visible for in seconds.
     * @param plugin the plugin sending the message.
     */
    public static void sendActionBarMessage(@NotNull final Player bukkitPlayer, @NotNull final String message,
                                            @NotNull final int duration, @NotNull Plugin plugin) {
        cancelPendingMessages(bukkitPlayer);
        final BukkitTask messageTask = new BukkitRunnable() {
            private int count = 0;
            @Override
            public void run() {
                if (count >= (duration - 3)) {
                    this.cancel();
                }
                sendActionBarMessage(bukkitPlayer, message);
                count++;
            }
        }.runTaskTimer(plugin, 0L, 20L);
        PENDING_MESSAGES.put(bukkitPlayer, messageTask);
    }

    private static void cancelPendingMessages(@NotNull Player bukkitPlayer) {
        if (PENDING_MESSAGES.containsKey(bukkitPlayer)) {
            PENDING_MESSAGES.get(bukkitPlayer).cancel();
        }
    }
}
