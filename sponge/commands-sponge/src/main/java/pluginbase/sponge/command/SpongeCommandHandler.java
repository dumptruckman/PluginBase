package pluginbase.sponge.command;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.completion.CommandCompleter;
import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;
import pluginbase.minecraft.BasePlayer;
import pluginbase.sponge.minecraft.SpongeTools;

import java.util.List;

public class SpongeCommandHandler extends CommandHandler implements CommandCompleter {

    public SpongeCommandHandler(@NotNull CommandProvider commandProvider) {
        super(commandProvider);
    }

    @Override
    protected boolean register(@NotNull CommandRegistration commandInfo, @NotNull Command command) {
        return false;
    }

    @NotNull
    @Override
    public List<String> getSuggestions(@NotNull CommandSource commandSource, @NotNull String args) throws CommandException {
        final BasePlayer wrappedSender = SpongeTools.wrapSender(commandSource);
        return tabComplete(wrappedSender, args.split("\\s"));
    }
}
