package pluginbase.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandUtil {

    public static void runCommand(Plugin plugin, CommandSender sender, String name, String... args) {
        Command mockCommand = mock(Command.class);
        when(mockCommand.getName()).thenReturn(name);
        final String message = name + " " + StringUtil.joinString(args, " ");
        if (sender instanceof Player) {
            PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent((Player) sender, message);
            plugin.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        final String[] command = name.split(".");
        final String[] fullArgs = new String[command.length + args.length];
        System.arraycopy(command, 0, fullArgs, 0, command.length);
        System.arraycopy(args, 0, fullArgs, command.length, args.length);
        plugin.onCommand(sender, mockCommand, "", fullArgs);
    }


}
