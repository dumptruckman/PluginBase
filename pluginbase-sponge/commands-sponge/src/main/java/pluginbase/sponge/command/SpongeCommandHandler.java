package pluginbase.sponge.command;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;

public class SpongeCommandHandler extends CommandHandler {

    public SpongeCommandHandler(@NotNull CommandProvider commandProvider) {
        super(commandProvider);
    }

    @Override
    protected boolean register(@NotNull CommandRegistration commandInfo, @NotNull Command command) {
        SpongeCommand spongeCommand = new SpongeCommand(commandProvider, command, commandInfo.getUsage(), commandInfo.getDesc());
        return Sponge.getCommandManager().register(commandProvider.getPlugin(), spongeCommand, commandInfo.getAliases()).isPresent();
    }

    /*
    @NotNull
    @Override
    public List<String> getSuggestions(@NotNull CommandSource commandSource, @NotNull String args) throws CommandException {
        final BasePlayer wrappedSender = SpongeTools.wrapSender(commandSource);
        return tabComplete(wrappedSender, args.split("\\s"));
    }
    */
}
