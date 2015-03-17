package pluginbase.testplugin.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.command.Command;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;

@CommandInfo(
        primaryAlias = "teal pea pie",
        desc = "Weirdly named command #2.",
        usage = "[happy]"
)
public class TealPeaPieCommand extends Command {

    public TealPeaPieCommand(@NotNull CommandProvider commandProvider) {
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
        sender.sendMessage("test worked.");
        return true;
    }
}
