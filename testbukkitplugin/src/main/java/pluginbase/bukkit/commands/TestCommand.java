package pluginbase.bukkit.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.bukkit.Language;
import pluginbase.bukkit.Language.Nest;
import pluginbase.bukkit.TestPlugin;
import pluginbase.command.Command;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandProvider;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;

@CommandInfo(
        primaryAlias = "test",
        desc = "Just a test command."
)
public class TestCommand extends Command<TestPlugin> {

    public TestCommand(@NotNull CommandProvider<TestPlugin> commandProvider) {
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
        getMessager().message(sender, Nest.NESTED_TEST_MESSAGE);
        return true;
    }
}
