package pluginbase.testbukkitplugin.command;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.command.Command;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.testbukkitplugin.TestPlugin;

@CommandInfo(
        primaryAlias = "teal pie",
        desc = "Weirdly named command."
)
public class TealPieCommand extends Command<TestPlugin> {

    public TealPieCommand(@NotNull CommandProvider<TestPlugin> commandProvider) {
        super(commandProvider);
    }

    @Nullable
    @Override
    public Perm getPerm() {
        return null;
    }

    @Nullable
    @Override
    public Message getHelp() {
        return null;
    }

    @Override
    public boolean runCommand(@NotNull BasePlayer sender, @NotNull CommandContext context) {
        int i = 0;
        StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                builder.append(context.getString(i));
                i++;
            } catch (Throwable ignore) {
                break;
            }
        }
        PluginCommand command = getPlugin().getCommand(builder.toString());
        sender.sendMessage(command != null ? command.getExecutor().toString() + " " + command.getTabCompleter() : "null");
        return true;
    }
}
