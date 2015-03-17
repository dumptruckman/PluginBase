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
import pluginbase.testplugin.Language;

@CommandInfo(
        primaryAlias = "test",
        desc = "Just a test command."
)
public class TestCommand extends Command {

    public TestCommand(@NotNull CommandProvider commandProvider) {
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
        getMessager().message(sender, Language.TEST_MESSAGE);
        getMessager().message(sender, Language.Nest.NESTED_TEST_MESSAGE);
        return true;
    }
}
