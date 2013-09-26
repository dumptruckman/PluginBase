package pluginbase.command.builtin;

import pluginbase.command.Command;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandProvider;
import pluginbase.messages.messaging.Messaging;
import pluginbase.minecraft.BasePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BuiltInCommand<P extends CommandProvider & Messaging> extends Command<P> {

    protected BuiltInCommand(@NotNull final P plugin) {
        super(plugin);
    }

    @NotNull
    public abstract List<String> getStaticAliases();

    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);
}
